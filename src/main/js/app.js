import React from "react";
import * as ReactDOMClient from 'react-dom/client';

class MessageBox extends React.Component {
  render() {
    return (
      <div>
          "Hello!"
      </div>
    );
  }
}

const root = ReactDOMClient.createRoot(document.getElementById("react-mountpoint"));
root.render(<MessageBox />);