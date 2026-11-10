import { Search } from "lucide-react";
import { useState } from "react";

function SearchBar({ onSearch }) {

  const [query, setQuery] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(query);
  };

  return (
    <form className="search-bar" onSubmit={handleSubmit}>

      <Search size={25} />

      <input
        type="text"
        placeholder="Rechercher une formation, un métier..."
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />

      <button type="submit">
        Rechercher
      </button>

    </form>
  );
}

export default SearchBar;