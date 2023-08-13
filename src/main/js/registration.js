import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class SignupForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      email: "",
      password: ""
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
    console.log(">>>" + this.state.email + ">" + this.state.username + ">" + this.state.password + ">");
  }

  render() {
    return (
      <div className="signup">
        <div className="form-container">
          <h1>Messenger</h1>
          <h2>Create Account</h2>
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
