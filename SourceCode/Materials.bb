Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	If Len(file)>0 Then
		Local i%
		For i%=1 To Len(file)
			mi=Mid(file$,i,1)
			If mi="\" Or mi="/" Then
				lastSlash=i
			EndIf
		Next
	EndIf

	Return Left(file,lastSlash)
End Function

Function GetTextureFromCache%(name$)
	Local tc.Materials
	For tc.Materials=Each Materials
		If Lower(tc\name) = Lower(name) Then Return tc\diff
	Next
	Return 0
End Function

Function GetCache.Materials(name$)
	Local tc.Materials
	For tc.Materials=Each Materials
		If Lower(tc\name) = Lower(name) Then Return tc
	Next
	Return Null
End Function

Function AddTextureToCache(name$,texture%)
	Local tc.Materials=GetCache(name)
	If tc.Materials=Null Then
		tc.Materials=New Materials
		tc\name=Lower(name)

		tc\diff=0
	EndIf
	If tc\diff=0 Then tc\diff=texture
End Function

Function ClearTextureCache()
	Local tc.Materials
	For tc.Materials=Each Materials
		If tc\diff<>0 Then FreeTexture tc\diff
		;If tc\bump<>0 Then FreeTexture tc\bump
		Delete tc
	Next
End Function

Function FreeTextureCache()
	Local tc.Materials
	For tc.Materials=Each Materials
		If tc\diff<>0 Then FreeTexture tc\diff
		;If tc\bump<>0 Then FreeTexture tc\bump
		tc\diff = 0; : tc\bump = 0
	Next
End Function

Function LoadRMeshTexture%(roompath$,name$,flags%)
	Local texture% = 0
	If texture=0 Then texture = LoadTexture(roompath+name+".jpg",flags)
	If texture=0 Then texture = LoadTexture(roompath+name+".png",flags)
	If texture=0 Then texture = LoadTexture("GFX/map/Textures/"+name+".jpg",flags)
	If texture=0 Then texture = LoadTexture("GFX/map/Textures/"+name+".png",flags)
	If texture<>0 Then
		DebugLog TextureName(texture)
	Else
		texture = LoadTexture("GFX/map/Textures/dirtymetal.jpg",flags);RuntimeError name
	EndIf
	Return texture
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D