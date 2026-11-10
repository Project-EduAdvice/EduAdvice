import { useState } from "react";

import Navbar from "./components/Navbar";
import EduAdvice from "./components/EduAdvice";
import Choice from "./components/Choice";
import Footer from "./components/Footer";

import "./App.css";

const TEAM = [
  {name: "Nom1", role: "Role 1"},
  {name: "Nom2", role: "Role 2"},
  {name: "Nom3", role: "Role 3"},
  {name: "Nom4", role: "Role 4"},
];

function App() {
  return (
    <div className="app">

      <Navbar />

      <main className="container">

        <EduAdvice onSearch={() => {}} />

        <Choice />
      </main>
        
      <Footer/>
  
    </div>
  );
}

export default App;