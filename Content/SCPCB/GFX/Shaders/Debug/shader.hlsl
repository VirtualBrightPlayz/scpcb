cbuffer cbVertex {
    matrix viewMatrix;
    matrix projectionMatrix;
};

cbuffer cbFragment {
    float4 inColor;
};

struct VS_INPUT {
    float4 position : POSITION0;
    float3 normal : NORMAL0;
};

struct PS_INPUT {
    float4 position : SV_POSITION;
    float3 normal : NORMAL0;
};

struct PS_OUTPUT {
    float4 color : SV_Target0;
    //float4 depth : SV_Target1;
};

PS_INPUT VS(VS_INPUT input) {
    PS_INPUT output = (PS_INPUT)0;
    output.position = float4(input.position.x, input.position.y, input.position.z, 1.0);
    output.position = mul(output.position,viewMatrix);
    output.position = mul(output.position,projectionMatrix);
    output.normal = input.normal;
    return output;
}

PS_OUTPUT PS(PS_INPUT input) {
    PS_OUTPUT output = (PS_OUTPUT)0;
    float4 diffColor = inColor;
    diffColor = ((dot(input.normal, normalize(float3(1,1,1))) + 1.5) / 2.5) * diffColor;

    output.color = float4(diffColor.r, diffColor.g, diffColor.b, 1.0);
    return output;
}
