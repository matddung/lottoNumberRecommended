import logo from './logo.svg';
import './App.css';
import { useState } from 'react';

function App() {

  // let name = 'studyjun'

  let [name, setName] = useState('studyjun');

  return (
    <div className="App">
      <header className="App-header">
        <div>{ name }</div>
      </header>
    </div>
  );
}

export default App;
