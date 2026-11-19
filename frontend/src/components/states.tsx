import { Loader2 } from "lucide-react";
import { useEffect, useState, type ReactNode } from "react";

// Au-delà de ce délai, le chargement n'est plus « normal » : le backend est
// probablement en train de se réveiller (hébergement gratuit mis en veille).
const SLOW_LOADING_DELAY_MS = 5000;

export function LoadingState({ label = "Chargement…" }: { label?: string }) {
  const [isSlow, setIsSlow] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setIsSlow(true), SLOW_LOADING_DELAY_MS);
    return () => clearTimeout(timer);
  }, []);

  return (
    <div role="status" className="py-6 text-muted-foreground">
      <p className="flex items-center gap-2">
        <Loader2 className="size-4 animate-spin" aria-hidden="true" />
        {label}
      </p>
      {isSlow && (
        <p className="mt-2 text-sm">
          Le serveur démarre, cela peut prendre jusqu'à une minute…
        </p>
      )}
    </div>
  );
}

export function ErrorState({ message, onRetry }: { message: string; onRetry?: () => void }) {
  return (
    <div role="alert" className="card-surface p-6">
      <p className="font-semibold">Une erreur est survenue</p>
      <p className="mt-1 text-sm text-muted-foreground">{message}</p>
      {onRetry && (
        <button
          type="button"
          onClick={onRetry}
          className="mt-4 rounded-xl bg-primary px-4 py-2 text-sm font-medium text-primary-foreground transition-colors hover:bg-primary/90"
        >
          Réessayer
        </button>
      )}
    </div>
  );
}

export function EmptyState({ children }: { children: ReactNode }) {
  return <p className="py-6 text-muted-foreground">{children}</p>;
}
