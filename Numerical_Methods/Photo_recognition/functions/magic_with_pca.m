## Copyright (C) 2021 Andrei
##
## This program is free software: you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation, either version 3 of the License, or
## (at your option) any later version.
##
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with this program.  If not, see <https://www.gnu.org/licenses/>.

## -*- texinfo -*-
## @deftypefn {} {@var{retval} =} magic_with_pca (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Andrei <Andrei@DESKTOP-PK505U9>
## Created: 2021-09-08

function [train, miu, Y, Vk] = magic_with_pca(train_mat, pcs)
  [m, n] = size(train_mat);
  
  % initializare train
  train = zeros(m, n);
  
  % initializare miu
  miu = zeros(1, n);
  
  % initializare Y
  Y = zeros(m, pcs);
  
  % initializare Vk
  Vk = zeros(n, pcs);
  
  % trec la double
  train_mat = double(train_mat);
  
  % media pe coloane
  miu = mean(train_mat, 1);
  
  % scad media
  train_mat = train_mat - miu;
  
  % calculez matricea de covarianta
  cov_mat = train_mat' * train_mat / (m - 1);
  
  % calculez vectorii si valorile proprii pentru matricea de covarianta
  [V, D] = eig(cov_mat);
  
  % sortez valorile proprii descrescator si creez matricea Vk cu vectorii proprii corespunzatori
  [eigenvalues, indices] = sort(diag(D), 'descend');
  V = V(:, indices);
  
  % pastrez decat primele pcs coloane
  Vk = V(:, 1:pcs);
  
  % creez matricea Y prin schimbarea bazei matricii originale
  Y = train_mat * Vk;
  
  % calculez matricea train
  train = Y * Vk';
end

