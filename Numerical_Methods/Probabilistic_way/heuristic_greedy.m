function [path] = heuristic_greedy (start_position, probabilities, Adj)
	path = start_position;
  [n, m] = size(Adj);
  visited=zeros(1, n);
  n -= 2;
  m -= 2;

  while ~isempty(path)
    position = path(end);
    % Am ajuns la Win
    if position == n + 1 
      return
    end

    neighbour = find(Adj(position, : ));
    max_prob = 0;
    n_position = 0;

    % Vecinii la care poate ajunge
    for k = 1:length(neighbour) 
      if ~visited(neighbour(k))
      % Nodul cu probabilitate maxima de castig
          if probabilities(neighbour(k)) > max_prob 
            max_prob = probabilities(neighbour(k));
            n_position = neighbour(k);
          endif
      endif
    endfor

    % Scad stiva drumului
    if n_position == 0
      path = path(1:end-1); 
    else
    % Marchez
      visited(n_position) = 1; 
    % Cresc stiva drumului
      path = [path, n_position]; 
    endif

  endwhile
endfunction
