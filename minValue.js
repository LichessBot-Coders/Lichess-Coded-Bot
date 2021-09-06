private synchronized int minValue ( Po s i t i on po s i t i on , int currDepth ) f
i f ( cutOf fTe s t ( po s i t i on , currDepth ) ) f
return u t i l i t y ( po s i t i on , currDepth ) ;
g
2
int u t i l = winVal ;
int cur r ;
short [ ] moves = p o s i t i o n . getAl lMoves ( ) ;
for ( int i = 0 ; i < moves . l ength ; i++) f
try f
p o s i t i o n . doMove (moves [ i ] ) ;
cur r = maxValue ( po s i t i on , currDepth ) ;
p o s i t i o n . undoMove ( ) ;
i f ( cur r < u t i l )
u t i l = cur r ;
g catch ( Except ion e ) f
System . out . p r i n t l n ( " I l l e g a l move found . . . " ) ;
g
g
return u t i l ;
g 
