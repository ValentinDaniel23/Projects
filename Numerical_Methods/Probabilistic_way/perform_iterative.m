function [x, err, steps] = perform_iterative (G, c, x0, tol, max_steps)
	n = length(c);
   
   x = x0;
   steps = 0;
   
   err = inf;
   diff = inf;
   
   % Folosesc un while cu mai multe conditii de oprire
   while norm(err - tol) > 0 && steps < max_steps && diff > tol
     % iteratia urmatoare
     x_new = G * x + c;
     
     diff = norm(x_new - x);
     % diferenta dintre solutii
     err = diff/norm(x_new);

     x = x_new;
     steps = steps + 1;
    end

endfunction
