function [classes] = predict_classes(X, weights, ...
                  input_layer_size, hidden_layer_size, ...
                  output_layer_size)
 
 % Se repeta pasii de la cost_function
 m = size(X, 1);
 A1 = [ones(m, 1) X];
 Z2 = A1 * weights.Theta1';
 A2 = sigmoid(Z2);
 A2 = [ones(m, 1) A2];
 A3 = sigmoid(A2 * weights.Theta2');

 [~, classes] = max(A3, [], 2);

endfunction
