const BASIC_LEVELS = Array.from({ length: 8 }, (_, index) => `${index + 1}\u00b0 B\u00e1sico`);
const HIGH_SCHOOL_LEVELS = Array.from({ length: 4 }, (_, index) => `${index + 1}\u00b0 Medio`);
const SECTIONS = ['A', 'B', 'C'];

export const COURSE_OPTIONS = [...BASIC_LEVELS, ...HIGH_SCHOOL_LEVELS]
  .flatMap((level) => SECTIONS.map((section) => `${level} ${section}`));
