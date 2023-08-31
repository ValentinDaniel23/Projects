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
## @deftypefn {} {@var{retval} =} classifyImage (@var{input1}, @var{input2})
##
## @seealso{}
## @end deftypefn

## Author: Andrei <Andrei@DESKTOP-PK505U9>
## Created: 2021-09-08

function prediction = classifyImage(im, train_mat, train_val, pcs)
  % initializare
  prediction = 0;
  
  % trecem la double
  im = double(im);
  
  % aplic magic_with_pca
  [train, miu, Y, V_k] = magic_with_pca(train_mat, pcs);
  
  % scad media
  im = im - miu;
  
  % schimb baza
  im = im * V_k;
  
  % calculez predictia cu KNN cu k = 5
  prediction = KNN(train_val, Y, im, 5);
end

