package kh.GiveHub.image.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Image {
	private int imgNo;
	private String imgPath;
	private String imgAlt;
	private String imgName;
	private String imgType;
	private int refNo;
}
