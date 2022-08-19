// all method using prototype in Typescript, declare here

declare global {
  interface String {
    toCamelCase(): string;
    isNullOrEmpty(source): boolean;
    equalsIgnoreCase(source: string, destination: string): boolean;
    includesIgnoreCase(source: string, destination: string): boolean;
    trimAllSpace(source: string): string;
  }
}

String.prototype.toCamelCase = function (): string {
  return this.replace(/(?:^\w|[A-Z]|-|\b\w)/g, (ltr, idx) => (idx === 0 ? ltr.toLowerCase() : ltr.toUpperCase())).replace(/\s+|-/g, '');
};
String.prototype.isNullOrEmpty = function (source): boolean {
  return !source || source.length === 0 || !source.trim();
};
String.prototype.equalsIgnoreCase = function (source: string, des: string): boolean {
  if (!source || !des) {
    return false;
  }
  return source.toUpperCase() === des.toUpperCase();
};
String.prototype.includesIgnoreCase = function (source: string, des: string): boolean {
  if (!source || des == null) {
    return false;
  }
  return source.toUpperCase().includes(des.toUpperCase());
};
String.prototype.trimAllSpace = function (source: string): string {
  if (!source) {
    return source;
  }
  return source.replace(/\s+/g, ' ').replace(/^\s+|\s+$/g, '');
};

export {};
