import { useEffect } from "react";
import { Outlet, Route, Routes, useLocation } from "react-router-dom";
import { Header } from "./components/layout/Header";
import { Footer } from "./components/layout/Footer";
import { Careers } from "./pages/Careers";
import { NotFound } from "./pages/NotFound";
import { ParcoursDetail } from "./pages/ParcoursDetail";
import { SubjectDetail } from "./pages/SubjectDetail";
import { TeachingUnitDetail } from "./pages/TeachingUnitDetail";
import App from "./App";

// Remonte en haut de la page à chaque changement de page,
// ou descend jusqu'à la section demandée quand l'adresse contient un #ancre (ex. /#formations)
function ScrollToTop() {
  const { pathname, hash, key } = useLocation();
  useEffect(() => {
    const target = hash ? document.getElementById(hash.slice(1)) : null;
    if (target) target.scrollIntoView();
    else window.scrollTo(0, 0);
  }, [pathname, hash, key]);
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
        <Route path="programmes/:id" element={<ParcoursDetail />} />
        <Route path="ue/:id" element={<TeachingUnitDetail />} />
        <Route path="cours/:id" element={<SubjectDetail />} />
        <Route path="metiers" element={<Careers />} />
        <Route path="*" element={<NotFound />} />
      </Route>
    </Routes>
  );
}