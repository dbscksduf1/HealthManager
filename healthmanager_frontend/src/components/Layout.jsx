import Navbar from "./Navbar";
import { useLocation } from "react-router-dom";

function Layout({ children }) {
  const location = useLocation();

  const hideNavbarPages = ["/", "/join"];

  const hide = hideNavbarPages.includes(location.pathname);

  return (
    <>
      {!hide && <Navbar />}
      {children}
    </>
  );
}

export default Layout;
