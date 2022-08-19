export function isToday(date) {
  if (!date) {
    return;
  }
  const inputDate = new Date(date);
  const today = new Date();
  return inputDate.setHours(0, 0, 0, 0) == today.setHours(0, 0, 0, 0);
}
