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

Type IntArrayList
	Field bank%
	
	Field size%
	Field capacity%
End Type

Const ARRAYLIST_INITIAL_CAPACITY% = 8

Function CreateIntArrayList.IntArrayList()
	Local arrayList.IntArrayList = New IntArrayList
	arrayList\bank = CreateBank(INT_SIZE * ARRAYLIST_INITIAL_CAPACITY)
	arrayList\size = 0
	arrayList\capacity = ARRAYLIST_INITIAL_CAPACITY
	Return arrayList
End Function

Function GetIntArrayListElem(arrayList.IntArrayList,i%)
	Return PeekInt(arrayList\bank,i*INT_SIZE)
End Function

Function SetIntArrayListElem(arrayList.IntArrayList,val%,i%)
	PokeInt(arrayList\bank,i*INT_SIZE,val)
End Function

Function PushIntArrayListElem(arrayList.IntArrayList,val%)
	If (arrayList\size>=arrayList\capacity) Then
		arrayList\capacity=arrayList\capacity*2
		ResizeBank(arrayList\bank, arrayList\capacity * INT_SIZE)
	EndIf
	
	SetIntArrayListElem(arrayList,val,arrayList\size)
	arrayList\size=arrayList\size+1
End Function

Function PopIntArrayListElem(arrayList.IntArrayList)
	arrayList\size=arrayList\size-1
	If arrayList\size<0 Then
		arrayList\size = 0
	EndIf
End Function

Function EraseIntArrayListElem(arrayList.IntArrayList,pos%)
	Local val%
	If pos>=arrayList\size Then Return
	For i% = pos To arrayList\size-2
		val = PeekInt(arrayList\bank,(i+1)*INT_SIZE)
		PokeInt(arrayList\bank,i*INT_SIZE,val)
	Next
	PopIntArrayListElem(arrayList)
End Function

Function DeleteIntArrayList(arrayList.IntArrayList)
	FreeBank arrayList\bank
	Delete arrayList
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D