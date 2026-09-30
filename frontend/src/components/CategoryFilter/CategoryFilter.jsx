import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFilter } from "@fortawesome/free-solid-svg-icons";

import "./CategoryFilter.css";

function CategoryFilter({ categories, value, onChange }) {
  return (
    <div className="category-filter">
      <FontAwesomeIcon icon={faFilter} className="category-filter-icon" />

      <select
        value={value}
        onChange={(event) => onChange(event.target.value)}
        aria-label="Filter by category"
      >
        <option value="">All Categories</option>

        {categories.map((category) => (
          <option key={category} value={category}>
            {category}
          </option>
        ))}
      </select>
    </div>
  );
}

export default CategoryFilter;
