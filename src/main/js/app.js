import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class MessageBox extends React.Component {
  handleKeyFetch = (event) => {
    event.preventDefault();

    callObjectStore("sender", (store) => {
      // store.add({privateKey: privateKeyBase64, publicKey: publicKeyBase64});
      const request = store.get(0);
      request.onerror = (event) => {
        console.error("Error loading credentials!");
      };
      request.onsuccess = (event) => {
        console.log("sender result:");
        console.log(request.result);
      }
    })
    callObjectStore("recipients", (store) => {
      const request = store.get("fakeId");
      request.onerror = (event) => {
        console.error("Error loading recipients info!");
      };
      request.onsuccess = (event) => {
        console.log("recipient result:");
        console.log(request.result);
      }
    })

  }

  handleKeyGen = async (event) => {
    event.preventDefault();

    const keyPairs = await generateKeyPairs();
    console.log("Key GEN 1!");
    console.log(keyPairs);

    const privateKeyBase64 = await privateKeyToBase64(keyPairs.privateKey);
    const publicKeyBase64 = await publicKeyToBase64(keyPairs.publicKey);
    console.log("Key GEN 3!");
    console.log(privateKeyBase64);
    console.log("Key GEN 4!");
    console.log(publicKeyBase64);

    const importedPublicKey = await importBase64PublicKey(publicKeyBase64);

    const encrypted = await encrypt("Hello, world!", importedPublicKey);
    console.log("Encrypt!");
    console.log(encrypted);
    const encryptedBuffer = String.fromCharCode.apply(null, new Uint8Array(encrypted));
    const sent = window.btoa(encryptedBuffer);
    console.log(sent);
    const decrypted = await decrypt(encrypted, keyPairs.privateKey);
    console.log("Decrypt!");
    console.log(decrypted);

    callObjectStore("sender", (store) => {
      // store.add({privateKey: privateKeyBase64, publicKey: publicKeyBase64});
      store.put({version: 0, privateKey: privateKeyBase64, publicKey: publicKeyBase64});
    })
    console.log("sender!");
    callObjectStore("recipients", (store) => {
      store.put({id: "fakeId", publicKey: publicKeyBase64});
    })
    console.log("recipients!");

  }
  render() {
    return (
      <div>
        <div className="buttons">
          <button className="generateKey" type="button" onClick={this.handleKeyGen}>Generate Key</button>
          <button className="FetchKey" type="button" onClick={this.handleKeyFetch}>Fetch Key</button>
        </div>
      </div>
    );
  }
}

const root = ReactDOMClient.createRoot(document.getElementById("react-mountpoint"));
root.render(<MessageBox />);

// Below are functions related to the web crypto API.

async function generateKeyPairs() {
  const algoritm = {
    name: "RSA-OAEP",
    modulusLength: 2048, //can be 1024, 2048, or 4096
    publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
    hash: {name: "SHA-256"}, //can be "SHA-1", "SHA-256", "SHA-384", or "SHA-512"
  };
  const keyPairs = await window.crypto.subtle.generateKey(
    algoritm,
    true, //whether the key is extractable (i.e. can be used in exportKey)
    ["encrypt", "decrypt"] //must be ["encrypt", "decrypt"] or ["wrapKey", "unwrapKey"]
  );
  return keyPairs;
}

async function privateKeyToBase64(privateKey) {
  const exported = await window.crypto.subtle.exportKey("pkcs8", privateKey);
  const exportedAsString = String.fromCharCode.apply(null, new Uint8Array(exported));
  const exportedAsBase64 = window.btoa(exportedAsString);

  return exportedAsBase64;
}

async function publicKeyToBase64(publicKey) {
  const exported = await window.crypto.subtle.exportKey("spki", publicKey);
  const exportedAsString = String.fromCharCode.apply(null, new Uint8Array(exported));
  const exportedAsBase64 = window.btoa(exportedAsString);

  return exportedAsBase64;
}

async function importBase64PublicKey(base64PublicKey) {
  const binaryString = window.atob(base64PublicKey);
  const byteArray = new Uint8Array(binaryString.length);
  for (var i = 0; i < binaryString.length; i++) {
    byteArray[i] = binaryString.charCodeAt(i);
  }

  const encryptAlg = {
    name: "RSA-OAEP",
    modulusLength: 2048, //can be 1024, 2048, or 4096
    publicExponent: new Uint8Array([0x01, 0x00, 0x01]),
    hash: {name: "SHA-256"}, //can be "SHA-1", "SHA-256", "SHA-384", or "SHA-512"
  };
  const publicKey = await window.crypto.subtle.importKey("spki", byteArray, encryptAlg, true, ["encrypt"])
  return publicKey;
}

async function encrypt(text, publicKey) {
  // Text to array buffer
  // var encoded = unescape(encodeURIComponent(str)) // 2 bytes for each char
  var buffer = new Uint8Array(text.length)
  for (let i = 0; i < text.length; i++) {
    buffer[i] = text.charCodeAt(i)
  }

  const rsaOaep = {
    name: "RSA-OAEP",
    // label: Uint8Array([...]) //optional
  };
  const encrypted = await window.crypto.subtle.encrypt(rsaOaep, publicKey, buffer);
  return encrypted;
}

async function decrypt(encrypted, privateKey) {
  const rsaOaep = {
    name: "RSA-OAEP",
    // label: Uint8Array([...]) //optional
  };
  const buffer = await window.crypto.subtle.decrypt(rsaOaep, privateKey, encrypted);

  // Array buffer to text
  const byteArray = new Uint8Array(buffer)
  return String.fromCharCode.apply(null, byteArray);
  // var str = ''
  // for (var i=0; i<byteArray.byteLength; i++) {
  //   str += String.fromCharCode(byteArray[i])
  // }
  // return str
}

// Below are functions related to the Indexed DB API.

function getIndexedDb() {
  return window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB
        || window.msIndexedDB || window.shimIndexedDB;
}

// The schema function is called whenever indexedDb.open() is invoked with a higher version
function indexedDbSchemaFn(event) {
  const db = event.target.result;
  try {
    db.deleteObjectStore("sender");
  } catch (ex) {
    console.log(">>>DOMException");
    console.log(ex);
  }
  try {
    db.deleteObjectStore("recipients");
  } catch (ex) {
    console.log(">>>DOMException");
    console.log(ex);
  }
  const senderStore = db.createObjectStore("sender", { keyPath: "version" });
  const recipientsStore = db.createObjectStore("recipients", { keyPath: "id" });
}

function callObjectStore(objStoreName, callbackFn) {
  const indexedDb = getIndexedDb();

  const request = indexedDb.open("Messenger", 1);

  request.onerror = (event) => {
    console.error("IndexedDB is not supported.");
  };

  // Delete and re-create object stores on version upgrade
  request.onupgradeneeded = indexedDbSchemaFn;

  request.onsuccess = (event) => {
  // Start a new transaction
    const db = event.target.result;
    const transaction = db.transaction(objStoreName, "readwrite");
    const store = transaction.objectStore(objStoreName);

    callbackFn(store);

    // Close the db when the transaction is done
    transaction.oncomplete = function() {
      db.close();
    };
  }
}
