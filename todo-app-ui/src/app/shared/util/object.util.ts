export const notNullOrUndefined = <T>(value: T | undefined | null): boolean =>
  value !== null && value !== undefined;

export const nullOrUndefined = <T>(value: T | undefined | null): boolean =>
  !notNullOrUndefined(value);

export const transform = (
  text: string,
  length: number = 20,
  suffix: string = '...'
): string => {
  if (text.length > length) {
    return text.substring(0, length).trim() + suffix;
  }

  return text;
};
