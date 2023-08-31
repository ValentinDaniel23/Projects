function [X, y] = load_dataset(path)
  data = load(path);
  
  % incarcarea lui X si y
  X = data.X;
  y = data.y;
endfunction
