Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	If Len(file)>0 Then
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
	For tc.Materials=Each Materials
		If Lower(tc\name) = Lower(name) Then Return tc\Diff
	Next
	Return 0
End Function

Function GetCache.Materials(name$)
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
		
		tc\Diff=0
	EndIf
	If tc\Diff=0 Then tc\Diff=texture
End Function

Function ClearTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		Delete tc
	Next
End Function

Function FreeTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		tc\Diff = 0; : tc\Bump = 0
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