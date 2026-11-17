import type { ReactNode } from "react";

export function PageShell({ children }: { children: ReactNode }) {
  return <main className="px-4 pt-8 sm:px-6 sm:pt-12 lg:px-[10%]">{children}</main>;
} 

export function SectionTitle({ eyebrow, title, children }: { eyebrow?: string; title: string; children?: ReactNode }) {
  return (
    <div className="mb-6">
      {eyebrow && <p className="text-sm font-semibold uppercase tracking-wider text-accent">{eyebrow}</p>}
      <h2 className="mt-1 text-2xl font-bold sm:text-3xl">{title}</h2>
      {children && <p className="mt-2 text-muted-foreground">{children}</p>}
    </div>
  );
}