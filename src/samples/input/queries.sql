SELECT * FROM Boats;
SELECT DISTINCT * FROM Sailors AS S, Reserves AS R, Boats AS B WHERE S.A = R.G AND R.G = B.F;
