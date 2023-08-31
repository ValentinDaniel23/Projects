function [Theta] = normal_equation(FeatureMatrix, Y, tol, iter)
   % Metoda gradientului conjugat pentru a rezolva sistemul (X'*X)*Theta = X'*Y
   % A = X'*X = FeaturesMatrix'*FeaturesMatrix și b = X'*Y = FeaturesMatrix'*Y
   A = FeatureMatrix' * FeatureMatrix;
   b = FeatureMatrix' * Y;
   
    % Initializarea variabilelor
    m = size(A, 2);
    x_0 = zeros(m, 1);
    r = b - A * x_0;
    v = r;
    Theta = x_0;
    tolsquared = tol^2;
    k = 1;
    
    % Verific daca e pozitiv definita
    eig_values = eig(A);
    if any(eig_values < 0)
        Theta = zeros(m + 1, 1);
        return
    end
    
    % Metoda gradientului conjugat
    while k < iter && r' * r > tolsquared
        tk = (r' * r) / (v' * A * v);
        Theta = Theta + tk * v;
        r_old = r;
        r = r_old - tk * A * v;
        sk = (r' * r) / (r_old' * r_old);
        v = r + sk * v;
        k = k + 1;
    end
    
    % Adaug coeficientul 0
    Theta = [0; Theta];
endfunction