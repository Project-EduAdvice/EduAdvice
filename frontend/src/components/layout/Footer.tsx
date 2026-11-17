const YEAR = new Date().getFullYear();

export function Footer() {
  return (
    <footer className="mt-24 border-t border-border bg-primary text-primary-foreground">
      <div className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
        <p className="text-sm text-primary-foreground/60 md:text-center">
          © {YEAR} EduAdvice. Tous droits réservés.
        </p>
      </div>
    </footer>
  );
}
