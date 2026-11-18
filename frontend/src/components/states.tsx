import { Loader2 } from "lucide-react";
import type { ReactNode } from "react";

export function LoadingState({ label = "Chargement…" }: { label?: string }) {
  return (
    <p role="status" className="flex items-center gap-2 py-6 text-muted-foreground">
      <Loader2 className="size-4 animate-spin" aria-hidden="true" />
      {label}
    </p>
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
