
Function sky_CreateSky(filename$,parent%=0)
	Local sky
	Local direction$
	Local x#,y#,z#,u#,v#
	Local b%,s%
	Local vert%
	
	sky = CreateMesh(parent)
	Local face%
	For face = 1 To 6
		Select face
			Case 1
				direction = "_back"
			Case 2
				direction = "_left"
			Case 3
				direction = "_front"
			Case 4
				direction = "_right"
			Case 5
				direction = "_up"
			Case 6
				direction = "_down"
		End Select
		Local fname$ = filename$ + direction$ + ".jpg"
		If FileType(fname$)=1 Then
			b% = LoadBrush(fname$,%110001)
			s% = CreateSurface(sky,b)
			
			Select face
				Case 1
					
				Case 2
					
				Case 3
					
				Case 4
					
				Case 5
					
				Case 6
					
			End Select
			AddTriangle s,0,1,2
			AddTriangle s,0,2,3
			FreeBrush b
		EndIf
	Next
	FlipMesh sky
	EntityFX sky,1+8
	EntityOrder sky,1000
	Return sky
End Function

Function UpdateSky()
	PositionEntity Sky, EntityX(mainPlayer\cam),EntityY(mainPlayer\cam),EntityZ(mainPlayer\cam), True
End Function

Function Update1499Sky()
	PositionEntity NTF_1499Sky, EntityX(mainPlayer\cam),EntityY(mainPlayer\cam),EntityZ(mainPlayer\cam), True
End Function


;-----------------------------------------------------------------------
;Data
;-----------------------------------------------------------------------

.sky_SkyboxData
Data "_back"
Data -1,+1,-1,0,0
Data +1,+1,-1,1,0
Data +1,-1,-1,1,1
Data -1,-1,-1,0,1
Data "_left"
Data +1,+1,-1,0,0
Data +1,+1,+1,1,0
Data +1,-1,+1,1,1
Data +1,-1,-1,0,1
Data "_front"
Data +1,+1,+1,0,0
Data -1,+1,+1,1,0
Data -1,-1,+1,1,1
Data +1,-1,+1,0,1
Data "_right"
Data -1,+1,+1,0,0
Data -1,+1,-1,1,0
Data -1,-1,-1,1,1
Data -1,-1,+1,0,1
Data "_up"
Data -1,+1,+1,0,0
Data +1,+1,+1,1,0
Data +1,+1,-1,1,1
Data -1,+1,-1,0,1
Data "_down"
Data -1,-1,-1,1,0
Data +1,-1,-1,1,1
Data +1,-1,+1,0,1
Data -1,-1,+1,0,0
;~IDEal Editor Parameters:
;~C#Blitz3D