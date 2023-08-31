function [X_train, y_train, X_test, y_test] = split_dataset(X, y, percent)
  
  % Se permuta liniile
  idx = randperm(size(X, 1));
  X = X(idx, :);
  y = y(idx);
  
  % Se alege procentul intreg
  num_train = round(percent * size(X, 1));
  
  % Se trimit datele
  X_train = X(1:num_train, :);
  y_train = y(1:num_train);
  X_test = X(num_train+1:end, :);
  y_test = y(num_train+1:end);

endfunction
