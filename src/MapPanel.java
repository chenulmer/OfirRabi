
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Console;
import java.sql.Time;
import java.util.LinkedList;
import java.util.Timer;

import javax.swing.JPanel;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.text.DefaultEditorKit.BeepAction;

import images.Img;

import map.Map;

public class MapPanel extends JPanel implements KeyListener, ActionListener {
	public static javax.swing.Timer _timer;
	private int _refreshRateX = 10, _refreshRateY = 10, _velX = 0, _velY = 0;
	private int _size;
	private int _sizeW;
	private int _blockSize;
	private Img _imgBackgound;
	private Img _floorBlock;
	private Img _topBlock;
	private Img _iceBlock;
	private Img _stoneBlock;
	private Img _insideBlock;
	private Img _waterButtomBlock;
	private Img _waterTopBlock;
	private Img _crystalBlock;
	private Img _crateBlock;
	private Img _snowMan;
	private Img _sign;
	private Map _map;
	private String _mapFile;
	private Img _blocks[];
	private LinkedList<Rectangle> _stopers;
	private Player _myPlayer;

	public MapPanel() {
		_timer = new javax.swing.Timer(1000 / 60, this);
		_stopers = new LinkedList<Rectangle>();
		_mapFile = "Maps\\map.xml";
		_size = Map.getElementCountByName(_mapFile, "Line");
		_sizeW = Map.getElementCountByName(_mapFile, "Area") / _size;
		_blockSize = 50;
		_blocks = new Img[12];
		_imgBackgound = new Img("images\\background.png", 0, 0, _sizeW * _blockSize, _size * _blockSize);
		_floorBlock = new Img("images\\floor.png", 0, 0, _blockSize, _blockSize);
		_blocks[3] = _floorBlock;// down floor with snow
		_topBlock = new Img("images\\top.png", 0, 0, _blockSize, _blockSize);
		_blocks[2] = _topBlock;// up block
		_iceBlock = new Img("images\\ice.png", 0, 0, _blockSize, _blockSize);
		_blocks[5] = _iceBlock;// ice cubes
		_stoneBlock = new Img("images\\stone.png", 0, 0, _blockSize, _blockSize);
		_blocks[4] = _stoneBlock;// stone
		_insideBlock = new Img("images\\inside.png", 0, 0, _blockSize, _blockSize);
		_blocks[1] = _insideBlock;// the base of the floor
		_waterButtomBlock = new Img("images\\waterButtom.png", 0, 0, _blockSize, _blockSize);
		_blocks[9] = _waterButtomBlock;// the base of the water
		_waterTopBlock = new Img("images\\waterTop.png", 0, 0, _blockSize, _blockSize);
		_blocks[7] = _waterTopBlock;// the waves of the water
		_crystalBlock = new Img("images\\crystal.png", 0, 0, _blockSize, _blockSize);
		_blocks[8] = _crystalBlock;// crystal
		_crateBlock = new Img("images\\crate.png", 0, 0, _blockSize, _blockSize);
		_blocks[6] = _crateBlock;// box
		_snowMan = new Img("images\\snowMan.png", 0, 0, _blockSize, _blockSize);
		_blocks[10] = _snowMan;// snow man
		_sign = new Img("images\\sign.png", 0, 0, _blockSize, _blockSize);
		_blocks[11] = _sign;// sign to continue
		_map = new Map(_size, _sizeW, _mapFile);
		_myPlayer = new Player(_blockSize);
		_timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		_stopers.clear();
		g.translate((int) -_myPlayer.getImage().getX() + _sizeW, 0);
		_imgBackgound.drawImg(g);
		for (int i = 0; i < _size; i++) {
			for (int j = 0; j < _sizeW; j++) {
				if (_map.get_map()[i][j] != 0 && (j * _blockSize) - _myPlayer.getImage().getX() > -10 * _blockSize) {
					_blocks[_map.get_map()[i][j]].setImgCords(j * _blockSize, i * _blockSize);
					// System.out.println("here"+j * _blockSize);
					_blocks[_map.get_map()[i][j]].drawImg(g);
					if (_map.get_map()[i][j] != 7) {
						Rectangle e = new Rectangle(j * _blockSize, i * _blockSize, _blockSize, _blockSize);
						_stopers.add(e);
					}
				}
			}
		}
		_myPlayer.getImage().setImgCords(_velX, 12 * _blockSize + _velY);
		_myPlayer.getImage().drawImg(g);

	}

	public void space() {
		_refreshRateY *= -1;
		_myPlayer.rotatePlayer();

	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_SPACE) {
			space();
		}
		if (code == KeyEvent.VK_ESCAPE) {
			_timer.stop();
		}
		repaint();

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_SPACE) {

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (getWidth() != 0 && getHeight() != 0) {
			switch (_myPlayer.move(_stopers, _refreshRateY)) {
			case 1:
				_velX += _refreshRateX;
				break;
			case 2:
				_velY += _refreshRateY;
				break;
			case 3:
				_velY += _refreshRateY;
				_velX += _refreshRateX;
				break;
			case 4:
				_velY += _refreshRateY;
				break;
			case 5:
				_velY += _refreshRateY;
				_velX += _refreshRateX;
				break;
			default:
				break;
			}
			repaint();
		}
	}

}
