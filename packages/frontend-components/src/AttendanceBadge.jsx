export default function AttendanceBadge({ label, value = 0, tone = 'neutral' }) {
  return (
    <article className={`attendance-badge attendance-badge--${tone}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}