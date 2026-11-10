import {
  GraduationCap,
  BriefcaseBusiness,
  ArrowRight
} from "lucide-react";

function Choice() {
  return (
    <section className="choice-grid">

      <a href="#formations" className="choice-card">

        <div className="choice-icon">
          <GraduationCap size={48} />
        </div>

        <div className="choice-content">
          <h2>Formations</h2>

          <p>
            Explorez les formations en informatique.
          </p>
        </div>

        <ArrowRight className="choice-arrow" />

      </a>

      <a href="#metiers" className="choice-card">

        <div className="choice-icon">
          <BriefcaseBusiness size={43} />
        </div>

        <div className="choice-content">
          <h2>Métiers</h2>
          <p>
            Découvrez les métiers
            liés a l'informatique.
          </p>
        </div>

        <ArrowRight className="choice-arrow" />

      </a>

    </section>
  );
}

export default Choice;