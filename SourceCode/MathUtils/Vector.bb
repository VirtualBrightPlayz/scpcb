Type Vector3f
	Field x#, y#, z#
	;TODO: hacky shit, remove once we move to C++
	Field persistent% = False
End Type

Function NewVector3f.Vector3f(x#,y#,z#)
	Local newVector.Vector3f = New Vector3f
	newVector\x = x : newVector\y = y : newVector\z = z
	newVector\persistent = False
	Return newVector
End Function

Function AddVector3f.Vector3f(a.Vector3f,b.Vector3f)
	Return NewVector3f(a\x+b\x,a\y+b\y,a\z+b\z)
End Function

Function MultiplyComponentsVector3f.Vector3f(a.Vector3f,b.Vector3f)
	Return NewVector3f(a\x*b\x,a\y*b\y,a\z*b\z)
End Function

Function CrossProductVector3f.Vector3f(a.Vector3f, b.Vector3f)
	Return NewVector3f(a\y*b\z-a\z*b\y,a\z*b\x-a\x*b\z,a\x*b\y-a\y*b\x)
End Function

Function LengthSquaredVector3f#(a.Vector3f)
	Return a\x*a\x+a\y*a\y+a\z*a\z
End Function

Function LengthVector3f#(a.Vector3f)
	Return Sqr(LengthSquaredVector3f(a))
End Function

Function EntityPosition.Vector3f(entity%,glbl%=False)
	Return NewVector3f(EntityX(entity,glbl),EntityY(entity,glbl),EntityZ(entity,glbl))
End Function

;TODO: remove once we move to C++
Function CleanupVector3f()
	Local vector.Vector3f
	For vector = Each Vector3f
		If (Not vector\persistent) Then Delete vector
	Next
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D