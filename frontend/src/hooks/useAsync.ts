import { useEffect, useState } from "react";

type AsyncState<T> =
  | { status: "loading" }
  | { status: "error"; error: unknown }
  | { status: "success"; data: T };

export function useAsync<T>(load: () => Promise<T>) {
  const [state, setState] = useState<AsyncState<T>>({ status: "loading" });
  const [attempt, setAttempt] = useState(0);

  useEffect(() => {
    let active = true;
    load().then(
      (data) => active && setState({ status: "success", data }),
      (error) => active && setState({ status: "error", error }),
    );
    return () => {
      active = false;
    };
    // `load` est recréée à chaque rendu : on ne relance que sur `attempt` (bouton Réessayer)
    // oxlint-disable-next-line react/exhaustive-deps
  }, [attempt]);

  const reload = () => {
    setState({ status: "loading" });
    setAttempt((n) => n + 1);
  };

  return { state, reload };
}