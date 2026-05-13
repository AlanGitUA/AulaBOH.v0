export default function AttendanceBadge({ label, value }) {
  return (
    <div className="attendance-badge">
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}
