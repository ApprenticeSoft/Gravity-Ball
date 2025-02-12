varying vec4 v_color;				//varying = partag� par le vertex shader et le fragment shader
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;		//sampler2D = prend la couleur d'un pixel d'une texture

void main(){
	vec4 color = v_color;
	
	gl_FragColor = texture2D(u_sampler2D, v_texCoord0) * vec4(0, 0, 0, 0.2*color.w);
}