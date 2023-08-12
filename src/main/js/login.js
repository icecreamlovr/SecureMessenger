import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class LoginForm extends React.Component {
  render() {
    return (
      <div className="login-form">
        <p> hello! there will be something in the future but for now its just this </p>
      </div>
    );
  }
}

const root = ReactDOMClient.createRoot(document.getElementById("react-mountpoint"));
root.render(<LoginForm />);
