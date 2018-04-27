Type IntArray2D
	Field bank%
	
	Field w%
	Field h%
End Type

Function CreateIntArray2D.IntArray2D(w%, h%)
	Local array.IntArray2D = New IntArray2D
	array\bank = CreateBank(4 * (w * h))
	array\w = w : array\h = h
	Return array
End Function

Function GetIntArray2DElem%(array.IntArray2D, x%, y%)
	Return PeekInt(array\bank, 4*(y*array\w+x))
End Function

Function SetIntArray2DElem(array.IntArray2D, x%, y%, val%)
	PokeInt(array\bank, 4*(y*array\w+x), val)
End Function

Function DeleteIntArray2D(array.IntArray2D)
	FreeBank array\bank
	Delete array
End Function