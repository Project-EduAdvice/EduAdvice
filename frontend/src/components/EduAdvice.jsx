import SearchBar from "./SearchBar";


function EduAdvice({onSearch}){
  return(
    <section className="hero">
        <div className="hero-content">
            <h1>EduAdvice</h1>
            <p>
                [A MODIFIER]
            </p>

            <p>
                Découvrez les formations, explorez les métiers 
                et trouvez le parcours qui vous correspond.
            </p>

        </div>

      <SearchBar onSearch={onSearch} />
    </section>
  );
}

export default EduAdvice;