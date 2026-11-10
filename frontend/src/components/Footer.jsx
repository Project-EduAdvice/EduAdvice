const TEAM = [
  {name: "Nom1", role: "Role 1"},
  {name: "Nom2", role: "Role 2"},
  {name: "Nom3", role: "Role 3"},
  {name: "Nom4", role: "Role 4"},
];

function Footer(){
  return (
    <footer id="team" className="footer">
      <div className="container">
        <p className="footer__title"> Team Members</p>
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

export default Footer;