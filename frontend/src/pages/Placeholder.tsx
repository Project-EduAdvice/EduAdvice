import { PageShell, SectionTitle } from "../components/layout/PageShell";

export function Placeholder({ title }: { title: string }) {
  return (
    <PageShell>
      <SectionTitle title={title}>Page en construction.</SectionTitle>
    </PageShell>
  );
}