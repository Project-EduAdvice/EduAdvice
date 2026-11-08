import "./App.css";

const TEAM = [
  {name: "Nom1", role: "Role 1"},
  {name: "Nom2", role: "Role 2"},
  {name: "Nom3", role: "Role 3"},
  {name: "Nom4", role: "Role 4"},
];


const THEMES = [
  {title:"Licence"},
  {title:"Master"},
  {title:"Debouches"},
];

function Nav(){
  return (
    <header className="navbar">
      <div className="container navbar__inner">
        <nav>
          <a href="/login" className="btn">Se connecter</a>
        </nav>
      </div>
    </header>
  );
}

function EduAdvice(){
  return(
    <section className="container EduAdvice">
      <h1>EduAdvice</h1>
      <p>
        [A MODIFIER]
      </p>
      <p>
        [Presentation EduAdvice]
      </p>
      <p>
        [Informatique en generale]
      </p>
      <p>
        [Informatique a l'USPN selon :]
      </p>
    </section>
  );
}

function Choix() {
  return (
    <section className="container themes">
      {THEMES.map((t) => (
        <div key={t.title} className="choix">
          <h3>{t.title}</h3>
        </div>
      ))}
    </section>
  );
}

function Footer(){
  return (
    <footer id="team" className="footer">
      <div className="container">
        <p className="footer__title"> Realise par :</p>
        <ul className="team">
          {TEAM.map((m, i) => (
            <li key={i}>
              <strong>{m.name}</strong>
              <span>{m.role}</span>
            </li>
          ))}
        </ul>
      </div>
    </footer>
  );
}

export default function App(){
  return (
    <>
      <Nav/>
      <main>
        <EduAdvice/>
        <Choix/>
      </main>
      <Footer/>
    </>
  );
};
