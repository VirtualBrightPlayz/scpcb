Include "SourceCode/MathUtils/Vector.bb"

Const INFINITY# = (999.0) ^ (99999.0) ;Bigger!
Const NAN# = (-1.0) ^ (0.5) ;Bigger?

Function Distance#(x1#, y1#, x2#, y2#)
	Local x# = x2 - x1
	Local y# = y2 - y1

	Return(Sqr(x*x + y*y))
End Function


Function CurveValue#(number#, old#, smooth#)
	If (timing\tickDuration = 0) Then Return old

	If (number < old) Then
		Return Max(old + (number - old) * (1.0 / smooth * timing\tickDuration), number)
	Else
		Return Min(old + (number - old) * (1.0 / smooth * timing\tickDuration), number)
	EndIf
End Function

Function CurveAngle#(val#, old#, smooth#)
	If (timing\tickDuration = 0) Then Return old

   Local diff# = WrapAngle(val) - WrapAngle(old)
   If (diff > 180) Then diff = diff - 360
   If (diff < - 180) Then diff = diff + 360
   Return WrapAngle(old + diff * (1.0 / smooth * timing\tickDuration))
End Function

Function WrapAngle#(angle#)
	If (angle = INFINITY) Then Return 0.0
	While (angle < 0)
		angle = angle + 360
	Wend
	While (angle >= 360)
		angle = angle - 360
	Wend
	Return angle
End Function

Function GetAngle#(x1#, y1#, x2#, y2#)
	Return ATan2( y2 - y1, x2 - x1 )
End Function

Function CircleToLineSegIsect% (cx#, cy#, r#, l1x#, l1y#, l2x#, l2y#)

	;Palauttaa:
	;  True (1) kun:
	;      Ympyrä [keskipiste = (cx, cy): säde = r]
	;      leikkaa janan, joka kulkee pisteiden (l1x, l1y) & (l2x, l2y) kaitta
	;  False (0) muulloin

	;Ympyrän keskipisteen ja (ainakin toisen) janan päätepisteen etäisyys < r
	;-> leikkaus
	If (Distance(cx, cy, l1x, l1y) <= r) Then
		Return True
	EndIf

	If (Distance(cx, cy, l2x, l2y) <= r) Then
		Return True
	EndIf

	;Vectors (vector of a line and vectors at end points of a segment at the center of a circle)
	Local SegVecX# = l2x - l1x
	Local SegVecY# = l2y - l1y

	Local PntVec1X# = cx - l1x
	Local PntVec1Y# = cy - l1y

	Local PntVec2X# = cx - l2x
	Local PntVec2Y# = cy - l2y

	;Em. the points of the vectors
	Local dp1# = SegVecX * PntVec1X + SegVecY * PntVec1Y
	Local dp2# = -SegVecX * PntVec2X - SegVecY * PntVec2Y

	;Checks whether the value of another point is 0
	;tai molempien merkki sama
	If (dp1 = 0 Or dp2 = 0) Then
	ElseIf ((dp1 > 0 And dp2 > 0) Or (dp1 < 0 And dp2 < 0)) Then
	Else
		;Neither -> no cutting
		Return False
	EndIf

	;The straight line passing through the endpoints ;equation; (ax + by + c = 0)
	Local a# = (l2y - l1y) / (l2x - l1x)
	Local b# = -1
	Local c# = -(l2y - l1y) / (l2x - l1x) * l1x + l1y

	;Distance from the center of the circle
	Local d# = Abs(a * cx + b * cy + c) / Sqr(a * a + b * b)

	;The circle is too far away
	;-> not cutting
	If (d > r) Then Return False

	;Local kateetin_pituus# = Cos(angle) * hyp

	;If you get this far, a circle and a line segment intersect (or are intertwined)
	Return True
End Function

Function Min#(a#, b#)
	If (a < b) Then
		Return a
	Else
		Return b
	EndIf
End Function

Function Max#(a#, b#)
	If (a > b) Then
		Return a
	Else
		Return b
	EndIf
End Function

Function angleDist#(a0#,a1#)
	Local b# = a0-a1
	Local bb#
	If (b<-180.0) Then
		bb = b+360.0
	ElseIf ((b>180.0)) Then
		bb = b-360.0
	Else
		bb = b
	EndIf
	Return bb
End Function

Function Inverse#(number#)

	Return Float(1.0 - number)

End Function

Function Rnd_Array#(numb1#,numb2#,Array1#,Array2#)
	Local whatarray% = Rand(1,2)

	If (whatarray = 1) Then
		Return Rnd(numb1, Array1)
	Else
		Return Rnd(Array2, numb2)
	EndIf

End Function

Function TimeInPosMilliSecs%()
	Local retVal% = MilliSecs()
	If (retVal < 0) Then retVal = retVal + 2147483648
	Return retVal
End Function

;--------------------------------------- MakeCollBox -functions -------------------------------------------------------

Global Mesh_MinX#, Mesh_MinY#, Mesh_MinZ#
Global Mesh_MaxX#, Mesh_MaxY#, Mesh_MaxZ#
Global Mesh_MagX#, Mesh_MagY#, Mesh_MagZ#

; Create a collision box For a mesh entity taking into account entity scale
; (will not work in non-uniform scaled space)
Function MakeCollBox(mesh%)
	Local sx# = EntityScaleX(mesh, 1)
	Local sy# = Max(EntityScaleY(mesh, 1), 0.001)
	Local sz# = EntityScaleZ(mesh, 1)
	GetMeshExtents(mesh)
	EntityBox(mesh, Mesh_MinX * sx, Mesh_MinY * sy, Mesh_MinZ * sz, Mesh_MagX * sx, Mesh_MagY * sy, Mesh_MagZ * sz)
End Function

; Find mesh extents
Function GetMeshExtents(Mesh%)
	Local s%, surf%, surfs%, v%, verts%, x#, y#, z#
	Local minx# = INFINITY
	Local miny# = INFINITY
	Local minz# = INFINITY
	Local maxx# = -INFINITY
	Local maxy# = -INFINITY
	Local maxz# = -INFINITY

	surfs = CountSurfaces(Mesh)

	For s = 1 To surfs
		surf = GetSurface(Mesh, s)
		verts = CountVertices(surf)

		For v = 0 To verts - 1
			x = VertexX(surf, v)
			y = VertexY(surf, v)
			z = VertexZ(surf, v)

			If (x < minx) Then minx = x
			If (x > maxx) Then maxx = x
			If (y < miny) Then miny = y
			If (y > maxy) Then maxy = y
			If (z < minz) Then minz = z
			If (z > maxz) Then maxz = z
		Next
	Next

	Mesh_MinX = minx
	Mesh_MinY = miny
	Mesh_MinZ = minz
	Mesh_MaxX = maxx
	Mesh_MaxY = maxy
	Mesh_MaxZ = maxz
	Mesh_MagX = maxx-minx
	Mesh_MagY = maxy-miny
	Mesh_MagZ = maxz-minz

End Function

Function EntityScaleX#(entity%, globl% = False)
	If (globl) Then
		TFormVector(1, 0, 0, entity, 0)
	Else
		TFormVector( 1, 0, 0, entity, GetParent(entity))
	EndIf
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function

Function EntityScaleY#(entity%, globl% = False)
	If (globl) Then
		TFormVector(0, 1, 0, entity, 0)
	Else
		TFormVector( 0, 1, 0, entity, GetParent(entity))
	EndIf
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function

Function EntityScaleZ#(entity%, globl% = False)
	If (globl) Then
		TFormVector(0, 0, 1, entity, 0)
	Else
		TFormVector(0, 0, 1, entity, GetParent(entity))
	EndIf
	Return Sqr(TFormedX() * TFormedX() + TFormedY() * TFormedY() + TFormedZ() * TFormedZ())
End Function

;----------------------------

Function SeedStringToInt%(seed$)
	Local char%
	Local retVal% = 0

	Local i%
	For i = 1 To Len(seed)
		char = Asc(Mid(seed,i,1))
		retVal = (retVal Shl 1) + char
	Next

	If (retVal=0) Then retVal = 1
	Return Abs(retVal)
End Function
;~IDEal Editor Parameters:
;~F#5#D#17#20#2B#6A#72#7A#87#8D
;~C#Blitz3D