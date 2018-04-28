Type IntArray
	Field bank%
	
	Field xDim%
	Field yDim%
	Field zDim%
End Type

Const INT_SIZE% = 4

Function CreateIntArray.IntArray(x%, y%=1, z%=1)
	Local array.IntArray = New IntArray
	array\bank = CreateBank(INT_SIZE * (x * y * z))
	array\xDim = x : array\yDim = y : array\zDim = z
	Return array
End Function

Function GetIntArrayElem%(array.IntArray, x%, y%=0, z%=0)
	Return PeekInt(array\bank, INT_SIZE*((z*array\yDim*array\xDim)+(y*array\xDim)+x))
End Function

Function SetIntArrayElem(array.IntArray, val%, x%, y%=0, z%=0)
	PokeInt(array\bank, INT_SIZE * ((z*array\yDim*array\xDim)+(y*array\xDim)+x), val)
End Function

Function DeleteIntArray(array.IntArray)
	FreeBank array\bank
	Delete array
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D