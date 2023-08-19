import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class LoginForm extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      email: "",
      password: ""
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
    console.log(">>>" + this.state.email +  ">" + this.state.password +  ">");
  }

  render() {
    return (
      <div className="Login">
        <div className="form-container">
          <h1>Messenger</h1>
          <h2>Log In :)</h2>
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
