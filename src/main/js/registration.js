import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class SignupForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      email: "",
      password: "",
      resultText: ""
    };
  }

  handleUsernameInput = (event) => {
    event.preventDefault();
    this.setState({
      username: event.target.value
    });
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

  handleSignup = (event) => {
    event.preventDefault();

    const baseUrl = "http://" + window.location.host;
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email: this.state.email, username: this.state.username, password: this.state.password })
    };

    fetch(baseUrl + "/signup", requestOptions)
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
        this.setState({resultText: "Signed up successfully!"});
      })
      .catch(error => {
        this.setState({resultText: "Signed up failed: " + error.message});
      });
  }

  render() {
    return (
      <div className="signup">
        <div className="form-container">
          <h1>Messenger</h1>
          <h2>Create Account</h2>
          <div className="signupResult"><label>{this.state.resultText}</label></div>
          <input type="text" id="username" placeholder="Name" onInput={this.handleUsernameInput}/>
          <input type="email" id="email" placeholder="Email" onInput={this.handleEmailInput}/>
          <input type="password" id="password" placeholder="Password"  onInput={this.handlePasswordInput}/>
          <button type="button" onClick={this.handleSignup}>Sign Up</button>
        </div>
      </div>
    );
  }
}

const root = ReactDOMClient.createRoot(document.getElementById("react-mountpoint"));
root.render(<SignupForm />);
