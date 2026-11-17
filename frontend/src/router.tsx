import { useEffect } from "react";
import { Outlet, Route, Routes, useLocation } from "react-router-dom";
import { Header } from "./components/layout/Header";
import { Footer } from "./components/layout/Footer";
import { NotFound } from "./pages/NotFound";
import { Placeholder } from "./pages/Placeholder";
import App from "./App";

// Remonte en haut de la page à chaque changement de page
function ScrollToTop() {
  const { pathname } = useLocation();
  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);
  return null;
}

function Layout() {
  return (
    <div className="flex min-h-screen flex-col">
      <ScrollToTop />
      <Header />
      <div className="flex-1">
        <Outlet />
      </div>
      <Footer />
    </div>
  );
}

export function AppRoutes() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<App />} />
        <Route path="programmes" element={<Placeholder title="Programmes" />} />
        <Route path="metiers" element={<Placeholder title="Métiers et débouchés" />} />
        <Route path="apropos" element={<Placeholder title="À propos" />} />
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
}