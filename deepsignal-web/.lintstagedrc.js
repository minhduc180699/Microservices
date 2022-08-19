module.exports = {
  '{,src/**/,webpack/}*.{md,json,yml,html,js,ts,tsx,css,scss,vue,java}': ['prettier --write'],
  '{,src/**/,webpack/}*.{json,html,js,ts,tsx,css,scss,vue}': ['npm run lint'],
};
