function [J, grad] = cost_function(params, X, y, lambda, ...
                   input_layer_size, hidden_layer_size, ...
                   output_layer_size)

Theta1 = reshape(params(1:hidden_layer_size * (input_layer_size + 1)), hidden_layer_size, (input_layer_size + 1));
Theta2 = reshape(params((1 + (hidden_layer_size * (input_layer_size + 1))):end), output_layer_size, (hidden_layer_size + 1));

m = size(X, 1);
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

A1 = [ones(m, 1) X];
Z2 = A1 * Theta1';
A2 = sigmoid(Z2);
A2 = [ones(m, 1) A2];

A3 = sigmoid(A2*Theta2');

Theta1_NoBias = Theta1(:,2:end);
Theta2_NoBias = Theta2(:,2:end);

Y = zeros(m, output_layer_size);
for i = 1:m
  Y(i, y(i)) = 1;
end

J = -1/m * sum(sum(Y .* log(A3) + (1 - Y) .* log(1-A3) ));

reg = lambda/(2*m) * (sum(sumsq(Theta1_NoBias)) + sum(sumsq(Theta2_NoBias)));
J = J + reg;

for t = 1:m
  a1 = A1(t,:);
  a2 = A2(t,:);
  a3 = A3(t,:);

  d3 = a3 - Y(t,:);
  z2 = Z2(t,:);

  g = zeros(size(z2));
  g = sigmoid(z2) .* (1 - sigmoid(z2));

  d2 = (d3 * Theta2_NoBias) .* g;

  Theta2_grad = Theta2_grad + d3'*a2;
  Theta1_grad = Theta1_grad + d2'*a1;
end

Theta1_grad = Theta1_grad / m;
Theta2_grad = Theta2_grad / m;

Theta1_grad(:, 2:end) = Theta1_grad(:, 2:end) + (lambda/m) * Theta1_NoBias;
Theta2_grad(:, 2:end) = Theta2_grad(:, 2:end) + (lambda/m) * Theta2_NoBias;

grad = [Theta1_grad(:); Theta2_grad(:)];

endfunction
