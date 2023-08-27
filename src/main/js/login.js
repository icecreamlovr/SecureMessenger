import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class LoginForm extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      email: "",
      password: "",
      resultText: ""
    };
  }

  handleEmailInput = (event) => {
    event.preventDefault();
    this.setState({
      email: event.target.value
    });
  }

  handlePasswordInput = (event) => {
    event.preventDefault();
    this.setState({
      password: event.target.value
    });
  }

   handleLogin = (event) => {
    event.preventDefault();

    const baseUrl = "http://" + window.location.host;
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email: this.state.email, password: this.state.password })
    };

    fetch(baseUrl + "/login", requestOptions)
      .then(response => {
        if (response.ok) {
          // Expect text on success
          return response.text();
        } else {
          // Expect JSON on error
          return response.json().then(response => {
            throw { status: response.status, message: response.message };
          });
        }
      })
      .then(data => {
        this.setState({resultText: "Login successfully!"});
      })
      .catch(error => {
        this.setState({resultText: "Login failed: " + error.message});
      });
  }

  render() {
    return (
      <div className="Login">
        <div className="form-container">
          <h1>Messenger</h1>
          <h2>Log In :)</h2>
          <div className="loginResult"><label>{this.state.resultText}</label></div>
          <input type="email" id="email" placeholder="Email" onInput={this.handleEmailInput}/>
          <input type="password" id="password" placeholder="Password" onInput={this.handlePasswordInput}/>
          <button type="button" onClick={this.handleLogin}>Log In</button>
        </div>
      </div>
    );
  }
}


const root = ReactDOMClient.createRoot(document.getElementById("react-mountpoint"));
root.render(<LoginForm />);
