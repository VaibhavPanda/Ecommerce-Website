import { useEffect, useState } from "react";

function useDebounce(value, delay = 400) {
  const [debouncedValue, setDebouncedValue] = useState(value);

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(timer);
    };
  }, [value, delay]);

  return debouncedValue;
}

export default useDebounce;


// n , ni, nik ,nike -> to prevent four times API call,
// wait 400ms before calling API
// if there is no typing within 400ms API is called
