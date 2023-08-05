import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class RegistrationForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      username: "",
      password: "",
    };
  }

  handleEmailInput = (event) => {
    event.preventDefault();
    this.setState({
      email: event.target.value
    });
  }

  handleUsernameInput = (event) => {
    event.preventDefault();
    this.setState({
      username: event.target.value
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
    console.log(">>>" + this.state.email + ">" + this.state.username + ">" + this.state.password + ">")
  }

  render() {
    return (
      <div className="registration-form">
        <h2>Messenger</h2>
        <div className="form-group">
          <label>Email</label>
          <input type="email" id="email" placeholder="Enter your email" onInput={this.handleEmailInput}/>
        </div>
        <div className="form-group">
          <label>Username</label>
          <input type="text" id="username" placeholder="Enter your username"  onInput={this.handleUsernameInput}/>
        </div>
        <div className="form-group">
          <label>Password</label>
          <input type="password" id="password" placeholder="Enter your password"  onInput={this.handlePasswordInput}/>
        </div>
        <div className="form-group">
          <button type="button" onClick = {this.handleSignup}>Sign Up</button>
        </div>
      </div>
    );
  }
}

const root = ReactDOMClient.createRoot(document.getElementById("react-mountpoint"));
root.render(<RegistrationForm />);
