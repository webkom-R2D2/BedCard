 package no.abakus.bedcard.gui.panels.content.konfigurering;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import no.abakus.bedcard.gui.BedCard;
import no.abakus.bedcard.gui.panels.content.BoxPanel;
import no.abakus.bedcard.gui.panels.content.BoxPanelInner;
import no.abakus.bedcard.gui.panels.content.ContentPanel;
import no.abakus.bedcard.gui.panels.content.ContentPanelMother;
import no.abakus.bedcard.gui.panels.content.konfigurering.threads.ConnectThread;
import no.abakus.bedcard.gui.panels.content.konfigurering.threads.LagreThread;
import no.abakus.bedcard.gui.panels.content.konfigurering.threads.LasteThread;
import no.abakus.bedcard.gui.panels.content.list.BedCardList;
import no.abakus.bedcard.gui.panels.content.list.BedCardSaveStateFilterList;
import no.abakus.bedcard.gui.panels.menu.BedCardButton;
import no.abakus.bedcard.gui.utils.ImageScaler;
import no.abakus.bedcard.gui.utils.SpringUtilities;
import no.abakus.bedcard.logic.Logic;
import no.abakus.bedcard.logic.cardreader.CardReader;
import no.abakus.bedcard.logic.cardreader.MagnetReader;
import no.abakus.bedcard.storage.domainmodel.BedCardSaveState;
import no.abakus.bedcard.storage.domainmodel.Event;
import no.abakus.bedcard.storage.domainmodel.Student;
import no.abakus.naut.entity.news.Type;

public class Konfigurering extends ContentPanelMother {
	private Student student;
	private JComboBox comboBox;
	private JFileChooser chooser;
	private JInternalFrame popupFrame;
	
	//Info panel (toppen)
	private JLabel tittel;
	private JLabel sted;
	private JLabel tid;
	private LogoButton logoButton;
	private JTextField antallPlasser;
	private JCheckBox friFlyt;
	private JCheckBox allowAll;
	private JButton lagreEventInput;
	private JLabel lagreToppenStatus;
	
	
	//Midten
	private JPanel switchPanel;
	private BedCardButton lesereKonfigBtn;
	private BedCardButton abakusKonfigBtn;
	private BedCardButton filKonfigBtn;
	
	//Bunnen
	private JButton abakusNoLagre;
	private JButton filLagre;
	private JButton nullstill;
	
	//AbakusNo
	private JTextField abakusNoFraDato;
	private JTextField abakusNoTilDato;
	private JComboBox abakusNoType;
	private JButton abakusNoHent;
	
	private JButton abakusNoLasteInn;
	private BedCardList abakusNoEvents;
	private SimpleDateFormat sdf;
	private AbakusNoKonfigureringActionListener abakusNoActionListener;
	
	//Fil
	private JComboBox filType;
	private JButton filHent;
	private JCheckBox filKunLagrede;
	private JButton filLasteInn;
	private BedCardList filEvents;
	private BedCardSaveStateFilterList filterFilEvents;
	
	//Lesere
	private JButton lesereReconnect;
	private JButton leserReconnect;
	private BedCardList lesereEvents;
	
	//Login
	private JTextField loginBrukernavn;
	private JPasswordField loginPassord;
	private JButton loginSett;
	
	private boolean faded;
	
	
	public Konfigurering(BedCard bedcard, Logic logic, ContentPanel contentPanel) {
		super(bedcard, logic, contentPanel);
		Font buttonFont = new Font("Tahoma", Font.PLAIN, 12);
		width = bedcard.getDrawWidth();
		height = bedcard.getDrawHeight()-BedCard.BottomStatusPanelHeight-BedCard.MenuPanelHeight;
		int boxHeight = 530;
		setPanelSizes();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		BoxPanel boxPanel = new BoxPanel(bedcard, "Innstillinger", 1024, boxHeight);
		int topPadding = (height-boxHeight)/2;
		setLayout(new FlowLayout(FlowLayout.CENTER, 0,topPadding));
		this.add(boxPanel);
		
		switchPanel = new JPanel();
		switchPanel.setLayout(new CardLayout(0,0));
		
		
		//Topp-panelet
		JPanel topPanelMain = new JPanel();
		topPanelMain.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
		topPanelMain.setBackground(Color.white);
		topPanelMain.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
		topPanelMain.setPreferredSize(new Dimension(1024, 85));
		boxPanel.getInternPanel().add(topPanelMain);
		
		JPanel topPanelLeft = new JPanel();
		topPanelLeft.setBackground(topPanelMain.getBackground());
		topPanelLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 10,0));
		topPanelLeft.setBorder(BorderFactory.createEmptyBorder());
		topPanelLeft.setPreferredSize(new Dimension(674, 83));
		topPanelMain.add(topPanelLeft);
		
		//Logo p� topp-panelet venstre
		logoButton = new LogoButton("LogoButton", bedcard, logic, topPanelLeft.getBackground(), 148, 83);
		logoButton.addActionListener(new KonfigureringActionListener());
		topPanelLeft.add(logoButton);
		
		//Spacing
		JPanel spacing = new JPanel();
		spacing.setPreferredSize(new Dimension(10, 83));
		spacing.setBackground(topPanelLeft.getBackground());
		topPanelLeft.add(spacing);
		
		//Beskrivelse p� topp-panelet venstre
		JPanel info = new JPanel();
		info.getInsets().set(0, 0, 0, 0);
		info.setBackground(topPanelLeft.getBackground());
		info.setBorder(BorderFactory.createEmptyBorder());
		info.setLayout(new BorderLayout(0,0));
		tittel = new JLabel();
		sted = new JLabel();
		tid = new JLabel();
		tittel.setFont(new Font("Tahoma",Font.PLAIN, 38));
		sted.setFont(new Font("Tahoma",Font.BOLD, 14));
		tid.setFont(new Font("Tahoma",Font.BOLD, 14));
		tid.setBackground(Color.green);
		info.add(tittel, BorderLayout.NORTH);
		info.add(tid, BorderLayout.CENTER);
		info.add(sted, BorderLayout.SOUTH);
		topPanelLeft.add(info);
		
		//Topp-panel h�yre
		JPanel topPanelRight = new JPanel();
		topPanelRight.setBackground(topPanelMain.getBackground());
		topPanelRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 15,0));
		topPanelRight.setPreferredSize(new Dimension(350, 83));
		topPanelMain.add(topPanelRight);
		
		antallPlasser = new JTextField("0", 3);
		antallPlasser.getDocument().addDocumentListener(new KonfigureringDocumentHandler());
		friFlyt = new JCheckBox();
		friFlyt.setBackground(topPanelRight.getBackground());
		friFlyt.addChangeListener(new KonfigureringChangeListener());
		
		allowAll = new JCheckBox();
		allowAll.setBackground(topPanelRight.getBackground());
		allowAll.addChangeListener(new KonfigureringChangeListener());
		
		lagreEventInput = new JButton("Sett");
		lagreEventInput.setBackground(topPanelRight.getBackground());
		lagreEventInput.addActionListener(new KonfigureringActionListener());
		lagreEventInput.setFont(buttonFont);
		
		
		JPanel antallPanel = new JPanel();
		antallPanel.setPreferredSize(new Dimension(100, 78));
		antallPanel.setLayout(new GridLayout(3,1,0,5));
		antallPanel.setBackground(topPanelRight.getBackground());
		JLabel antallLabel = new JLabel("Antall plasser:");
		Font font = new Font("Tahoma", Font.BOLD, 14);
		antallLabel.setFont(font);
		antallPanel.add(new JLabel());	
		antallPanel.add(antallLabel);
		JPanel antPlass = new JPanel();
		antPlass.setBackground(antallPanel.getBackground());
		antPlass.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		antPlass.add(antallPlasser);
		antallPanel.add(antPlass);
		topPanelRight.add(antallPanel);
		
		JPanel friFlytPanel = new JPanel();
		friFlytPanel.setPreferredSize(new Dimension(50, 78));
		friFlytPanel.setLayout(new GridLayout(3,1,0,5));
		friFlytPanel.setBackground(topPanelRight.getBackground());
		JLabel friFlytLabel = new JLabel("Fri flyt:");
		friFlytLabel.setFont(font);
		friFlytPanel.add(new JLabel());		
		friFlytPanel.add(friFlytLabel);
		JPanel friPlass = new JPanel();
		friPlass.setBackground(friFlytPanel.getBackground());
		friPlass.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		friPlass.add(friFlyt);
		friFlytPanel.add(friPlass);
		topPanelRight.add(friFlytPanel);
		
		JPanel allowAllPanel = new JPanel();
		allowAllPanel.setPreferredSize(new Dimension(65, 78));
		allowAllPanel.setLayout(new GridLayout(3,1,0,5));
		allowAllPanel.setBackground(topPanelRight.getBackground());
		JLabel allowAllLabel = new JLabel("Tillat alle:");
		allowAllLabel.setFont(font);
		allowAllPanel.add(new JLabel());		
		allowAllPanel.add(allowAllLabel);
		
		JPanel allowAllPlass = new JPanel();
		allowAllPlass.setBackground(allowAllPanel.getBackground());
		allowAllPlass.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		allowAllPlass.add(allowAll);
		allowAllPanel.add(allowAllPlass);
		topPanelRight.add(allowAllPanel);
		
		
		JPanel lagrePanel = new JPanel();
		lagrePanel.setPreferredSize(new Dimension(60, 78));
		lagrePanel.setBackground(topPanelRight.getBackground());
		lagrePanel.setLayout(new GridLayout(3,1,0,5));
		lagrePanel.add(new JLabel());
		lagreToppenStatus = new JLabel("");
		lagrePanel.add(lagreToppenStatus);
		lagrePanel.add(lagreEventInput);
		topPanelRight.add(lagrePanel);
		
		
		//Senterpanelet
		JPanel senterPanel = new JPanel();
		senterPanel.setBackground(boxPanel.getInternPanel().getBackground());
		int senterHeight = boxHeight-161;
		senterPanel.setPreferredSize(new Dimension(1024-BoxPanelInner.boxPadding*2-20, senterHeight));
		boxPanel.getInternPanel().add(senterPanel);
		senterPanel.setLayout(null);
		filKonfigBtn = new BedCardButton("filKonfig", bedcard.getImageLoader().getImage("innstillinger/filH.png"),bedcard.getImageLoader().getImage("innstillinger/fil.png"));
		filKonfigBtn.addActionListener(new KonfigureringSwitcherActionListener());
		abakusKonfigBtn = new BedCardButton("abakusKonfig", bedcard.getImageLoader().getImage("innstillinger/abakusH.png"),bedcard.getImageLoader().getImage("innstillinger/abakus.png"));
		abakusKonfigBtn.addActionListener(new KonfigureringSwitcherActionListener());
		lesereKonfigBtn = new BedCardButton("lesereKonfig", bedcard.getImageLoader().getImage("innstillinger/lesereH.png"),bedcard.getImageLoader().getImage("innstillinger/lesere.png"));
		lesereKonfigBtn.addActionListener(new KonfigureringSwitcherActionListener());
		
		JPanel konfigMenuPanel = new JPanel();
		konfigMenuPanel.setBackground(senterPanel.getBackground());
		konfigMenuPanel.setBounds(15,15,300, senterHeight-30);
		konfigMenuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, (senterHeight-30-80*3)/4));
		senterPanel.add(konfigMenuPanel);
		
		konfigMenuPanel.add(lesereKonfigBtn);
		konfigMenuPanel.add(abakusKonfigBtn);
		konfigMenuPanel.add(filKonfigBtn);
		
		
		switchPanel = new JPanel();
		switchPanel.setBackground(senterPanel.getBackground());
		//switchPanel.setBackground(Color.black);
		switchPanel.setBounds(330,15,652, senterHeight-30);
		switchPanel.setLayout(new CardLayout(0,0));
		senterPanel.add(switchPanel);
		
		BoxPanelInner lesere = new BoxPanelInner(bedcard, "Kortlesere", 652, senterHeight-30);
		lesere.getBottomPanel().setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 12));
		BoxPanelInner fil = new BoxPanelInner(bedcard, "Fil", 652, senterHeight-30);
		fil.getBottomPanel().setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 12));
		BoxPanelInner abakus = new BoxPanelInner(bedcard, "Abakus.no", 652, senterHeight-30);
		abakus.getBottomPanel().setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 12));
		switchPanel.add(lesere, "lesere");
		switchPanel.add(fil, "fil");
		switchPanel.add(abakus, "abakus");
		

		
		//Abakus.no
		abakus.getInternPanel().setLayout(new GridLayout(1,2,5,0));
		JPanel abakusLeft = new JPanel();
		abakusLeft.setBackground(abakus.getInternPanel().getBackground());
		abakusLeft.setLayout(new FlowLayout(FlowLayout.CENTER, 0,senterHeight-272));
		JPanel abakusRight = new JPanel();
		abakusRight.setLayout(new BorderLayout(0,5));
		abakusRight.setBackground(abakus.getInternPanel().getBackground());
		abakusRight.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(195,195,195)));
				
		abakus.getInternPanel().add(abakusRight);
		abakus.getInternPanel().add(abakusLeft);
		
		//Toppen
		JPanel abakusNoToppknapperGrid = new JPanel();
		abakusNoToppknapperGrid.setLayout(new GridLayout(2,1,0,0));
		JPanel abakusNoToppknapper = new JPanel();
		abakusNoToppknapper.setBackground(abakus.getInternPanel().getBackground());
		abakusNoToppknapper.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		
		JPanel abakusNoToppknapper2 = new JPanel();
		abakusNoToppknapper2.setBackground(abakus.getInternPanel().getBackground());
		abakusNoToppknapper2.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		
		abakusNoFraDato = new JTextField(8);
		abakusNoTilDato = new JTextField(8);
		
		sdf = new SimpleDateFormat("dd.MM.yyyy");
		Calendar cal = Calendar.getInstance();
		//Trekk fra f.eks en uke p� dagens dato og pluss p� fra dagens dato
		int showWindowToEachSide = 7*4;
		log.debug("time: " + showWindowToEachSide);
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-showWindowToEachSide);
		abakusNoFraDato.setText(sdf.format(cal.getTime()));
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+showWindowToEachSide*2);
		abakusNoTilDato.setText(sdf.format(cal.getTime()));
		abakusNoType = new JComboBox(logic.getAllTypes().toArray());
		abakusNoActionListener = new AbakusNoKonfigureringActionListener();
		abakusNoType.addActionListener(abakusNoActionListener);
				
		abakusNoToppknapper.add(new JLabel("Fra:"));
		abakusNoToppknapper.add(abakusNoFraDato);
		abakusNoToppknapper.add(new JLabel("Til:"));
		abakusNoToppknapper.add(abakusNoTilDato);
		abakusNoToppknapper2.add(new JLabel("Type:"));
		abakusNoToppknapper2.add(abakusNoType);
		abakusNoToppknapperGrid.add(abakusNoToppknapper);
		abakusNoToppknapperGrid.add(abakusNoToppknapper2);
		abakusRight.add(abakusNoToppknapperGrid, BorderLayout.NORTH);
		
		//Mellom
		abakusNoEvents = new BedCardList();
		abakusNoEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		abakusNoEvents.setCellRenderer(new EventListCellRenderer());
		abakusNoEvents.setPrototypeCellValue(new Event());
		abakusNoEvents.setAutoscrolls(true);
		abakusNoEvents.addMouseListener(new KonfigureringMouseListener());
		abakusNoEvents.addKeyListener(new EnterKeyListener());
		JScrollPane scrollPaneAbakusNo = new JScrollPane(abakusNoEvents);
		scrollPaneAbakusNo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneAbakusNo.setBorder(null);
		abakusRight.add(scrollPaneAbakusNo, BorderLayout.CENTER);
		
		//Bunnen
		abakusNoHent = new JButton("Oppdater");
		abakusNoLasteInn = new JButton("Last inn");
		abakusNoHent.setBackground(abakus.getBottomPanel().getBackground());
		abakusNoLasteInn.setBackground(abakus.getBottomPanel().getBackground());
		abakusNoHent.addActionListener(new AbakusNoKonfigureringActionListener());
		abakusNoLasteInn.addActionListener(new AbakusNoKonfigureringActionListener());
		abakusNoHent.setFont(buttonFont);
		abakusNoLasteInn.setFont(buttonFont);
		
		abakus.getBottomPanel().add(abakusNoHent);
		abakus.getBottomPanel().add(abakusNoLasteInn);
		
		//Abakus login
	
		//Center
		JPanel login = new JPanel();
		SpringLayout loginLayout = new SpringLayout();
		login.setLayout(loginLayout);
		login.setBackground(abakusLeft.getBackground());
		Font loginFont = new Font("Tahoma", Font.PLAIN, 14);
		JLabel loginBrukernavnText = new JLabel("Brukernavn:");
		loginBrukernavnText.setFont(loginFont);
		JLabel loginPassordText = new JLabel("Passord:");
		loginPassordText.setFont(loginFont);
		
		loginBrukernavn = new JTextField(15);
		loginPassord = new JPasswordField(15);
		loginBrukernavn.addKeyListener(new EnterKeyListener());
		loginPassord.addKeyListener(new EnterKeyListener());
		
		login.add(loginBrukernavnText);
		login.add(loginBrukernavn);
		
		login.add(loginPassordText);
		login.add(loginPassord);
		
		login.add(new JLabel(""));
		loginSett = new JButton("Tilkoble");
		loginSett.addActionListener(new AbakusNoKonfigureringActionListener());
		loginSett.setBackground(abakusLeft.getBackground());
		loginSett.setFont(buttonFont);
		JPanel sett = new JPanel();
		sett.setBackground(abakusLeft.getBackground());
		sett.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		sett.add(loginSett);
		login.add(sett);
		
		SpringUtilities.makeCompactGrid(login, 3,2,0,0,30,10);
		abakusLeft.add(login);

		//Fil-lagring
		
		fil.getInternPanel().setLayout(new BorderLayout(0,3));
		
		//Toppen

		JPanel filToppknapper = new JPanel();
		filToppknapper.setBackground(fil.getInternPanel().getBackground());
		filToppknapper.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		
		JPanel typePanel = new JPanel();
		typePanel.setBackground(filToppknapper.getBackground());
		typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		filType = new JComboBox();
		filType.addActionListener(new FilKonfigureringActionListener());
		
		JPanel filLagrePanel = new JPanel();
		filLagrePanel.setBackground(filToppknapper.getBackground());
		filLagrePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		filKunLagrede = new JCheckBox();
		filKunLagrede.setBackground(filToppknapper.getBackground());
		filKunLagrede.addActionListener(new FilKonfigureringActionListener());
		filKunLagrede.setSelected(true);
		typePanel.add(new JLabel("Type:"));
		typePanel.add(filType);
		filLagrePanel.add(new JLabel("Vis kun ulagret:"));
		filLagrePanel.add(filKunLagrede);
		filToppknapper.add(typePanel);
		filToppknapper.add(filLagrePanel);		
		fil.getInternPanel().add(filToppknapper, BorderLayout.NORTH);
		
		//Mellom
		filEvents = new BedCardList();
		filEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filEvents.setCellRenderer(new BedCardSaveStateListCellRenderer(bedcard));
		filEvents.setPrototypeCellValue(new BedCardSaveState());
		filEvents.setAutoscrolls(true);
		filEvents.addMouseListener(new KonfigureringMouseListener());
		filEvents.addKeyListener(new EnterKeyListener());
		filterFilEvents = new BedCardSaveStateFilterList();
		
		JScrollPane scrollPaneFil = new JScrollPane(filEvents);
		scrollPaneFil.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneFil.setBorder(null);
		fil.getInternPanel().add(scrollPaneFil, BorderLayout.CENTER);

		//Bunnen

		filHent = new JButton("Oppdater");
		filLasteInn = new JButton("Last inn");
		filHent.setBackground(fil.getBottomPanel().getBackground());
		filLasteInn.setBackground(fil.getBottomPanel().getBackground());
		filHent.addActionListener(new FilKonfigureringActionListener());
		filLasteInn.addActionListener(new FilKonfigureringActionListener());
		filHent.setFont(buttonFont);
		filLasteInn.setFont(buttonFont);
		fil.getBottomPanel().add(filHent);
		fil.getBottomPanel().add(filLasteInn);	
				

		//Lesere
		
		
		lesere.getInternPanel().setLayout(new BorderLayout(0,3));
		
		lesereEvents = new BedCardList();
		lesereEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lesereEvents.setCellRenderer(new ReaderListCellRenderer(bedcard));
		lesereEvents.setPrototypeCellValue(new MagnetReader(null));
		lesereEvents.setAutoscrolls(true);
		lesereEvents.addMouseListener(new KonfigureringMouseListener());
		lesereEvents.addKeyListener(new EnterKeyListener());
		
		JScrollPane scrollPaneLesere = new JScrollPane(lesereEvents);
		scrollPaneLesere.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLesere.setBorder(null);
		lesere.getInternPanel().add(scrollPaneLesere, BorderLayout.CENTER);
		
		//LESER RECONNECT
		leserReconnect = new JButton("Tilkoble på nytt");
		leserReconnect.setBackground(lesere.getBottomPanel().getBackground());
		leserReconnect.addActionListener(new LesereKonfigureringActionListener());
		leserReconnect.setFont(buttonFont);
		
		lesereReconnect = new JButton("Tilkoble alle på nytt");
		lesereReconnect.setBackground(lesere.getBottomPanel().getBackground());
		lesereReconnect.addActionListener(new LesereKonfigureringActionListener());
		lesereReconnect.setFont(buttonFont);

		lesere.getBottomPanel().add(leserReconnect);
		lesere.getBottomPanel().add(lesereReconnect);
		
		
		
		
		
		//Bunn-panelet
		boxPanel.getBottomPanel().setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 12));
				
		nullstill = new JButton("Fjern event");
		nullstill.setBackground(boxPanel.getBottomPanel().getBackground());
		nullstill.addActionListener(new KonfigureringActionListener());
		nullstill.setFont(buttonFont);
		
		abakusNoLagre = new JButton("Lagre");
		abakusNoLagre.setBackground(boxPanel.getBottomPanel().getBackground());
		abakusNoLagre.addActionListener(new KonfigureringActionListener());
		abakusNoLagre.setFont(buttonFont);
		
		filLagre = new JButton("Lagre kun til fil");
		filLagre.setBackground(boxPanel.getBottomPanel().getBackground());
		filLagre.addActionListener(new KonfigureringActionListener());
		filLagre.setFont(buttonFont);
		
			
		JPanel eventValg = new JPanel();
		eventValg.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		eventValg.setBackground(boxPanel.getBottomPanel().getBackground());

		JLabel eventValgTekst = new JLabel("Behandle event: ");
		eventValgTekst.setFont(new Font("Tahoma", Font.BOLD, 14));
		eventValgTekst.setForeground(Color.white);
		eventValg.add(eventValgTekst);
		eventValg.add(abakusNoLagre);
		eventValg.add(filLagre);
		eventValg.add(nullstill);
		boxPanel.getBottomPanel().add(eventValg);
		
		JPanel skjermValg = new JPanel();
		skjermValg.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		skjermValg.setBackground(boxPanel.getBottomPanel().getBackground());
		JLabel velgSkjerm = new JLabel("Velg skjerm: ");
		velgSkjerm.setForeground(Color.white);
		velgSkjerm.setFont(new Font("Tahoma", Font.BOLD, 14));
		skjermValg.add(velgSkjerm);
						
		//Valg av display
		ArrayList<Skjerm> skjermer = new ArrayList<Skjerm>();
		GraphicsDevice valgt = bedcard.getCurrentDevice();
		Skjerm valgtSkjerm = null;
		int skjermNr = 0;
		for(GraphicsDevice gd : bedcard.getDevices()){
			skjermNr++;
			Skjerm skjerm = new Skjerm(skjermNr, gd);
			skjermer.add(skjerm);
			if(valgt == gd){
				valgtSkjerm = skjerm;
			}
		}
		comboBox = new JComboBox(skjermer.toArray());
		comboBox.setSelectedItem(valgtSkjerm);
		comboBox.addActionListener(new KonfigureringActionListener());
		comboBox.setFont(buttonFont);	
		skjermValg.add(comboBox);
		
		boxPanel.getBottomPanel().add(skjermValg);

		//Filechooser
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG, PNG & GIF Images", "jpg", "gif", "png");
		chooser.setFileFilter(filter);
		//Get path
		String path = new File(".").getAbsolutePath() + "\\Logoer\\";
		File f1 = new File(path);
		if(!f1.exists()){
			f1.mkdir();
		}
		chooser.setSelectedFile(new File(path+"."));

		chooser.setAccessory(new ImagePreview(chooser));
		chooser.addActionListener(new JFileChooserActionListener());
		
		refreshFilTyper();
		refreshFiler();
		entered();
	}
	public void refreshData(){
		//Noe har skjedd, refresh
		
		if(logic.getEvent() != null){
			if(logic.getEvent().getTitle().length()>25){
				tittel.setText(logic.getEvent().getTitle().substring(0,24) + "...");
			} else {
				tittel.setText(logic.getEvent().getTitle());
			}
			sted.setText(logic.getEvent().getLocation());
			
			//"Tirsdag 6. November, kl 17:00"
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE d. MMMM");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
			tid.setText(sdf.format(logic.getEvent().getStartTime()) + ", kl " + sdf2.format(logic.getEvent().getStartTime()));
			
			antallPlasser.setText(""+logic.getEvent().getCapacity());
		} else {
			tittel.setText("");
			sted.setText("");
			tid.setText("");
			antallPlasser.setText("0");
		}
		friFlyt.setSelected(logic.getFreeFlow());
		allowAll.setSelected(logic.getAllowEverybody());
		logoButton.updateData();
				
		
		
		//Gj�r gui enklere, ved � disable ting som ikke skal v�re mulig enda.
		boolean eventErNull = logic.getEvent() != null;
		abakusNoLagre.setEnabled(logic.isAuthenticated()&&eventErNull&&!faded);
		lagreEventInput.setEnabled(eventErNull&&!faded);
		antallPlasser.setEnabled(eventErNull&&!faded);
		allowAll.setEnabled(eventErNull&&!faded);
		friFlyt.setEnabled(eventErNull&&!faded);
		filLagre.setEnabled(eventErNull&&!faded);
		nullstill.setEnabled(eventErNull&&!faded);
		repaint();
	}
	public void refreshAbakus(){
		abakusNoFraDato.setEnabled(logic.isAuthenticated()&&!faded);
		abakusNoTilDato.setEnabled(logic.isAuthenticated()&&!faded);
		abakusNoType.setEnabled(logic.isAuthenticated()&&!faded);
		abakusNoLasteInn.setEnabled(logic.isAuthenticated()&&!faded);
		abakusNoHent.setEnabled(logic.isAuthenticated()&&!faded);
		hentListeFraAbakus();
	}
	public void refreshAbakusTyper(){
		//N� har vi logget p�, hent typer:
		int selected = abakusNoType.getSelectedIndex();
		abakusNoType.removeActionListener(abakusNoActionListener);
		abakusNoType.removeAllItems();		
		Type bedpres = null;
		for(Type type : logic.getAllTypes()){
			log.debug(type.getName() + " value: " + type.getId().intValue());
			abakusNoType.addItem(type);
			if(type.getId().intValue() == 117)
				bedpres = type;
		}
		if(logic.getAllTypes().size()>0){
			if(selected>0){
				abakusNoType.setSelectedIndex(selected);
			} else {
				abakusNoType.setSelectedItem(bedpres);
			}
		}
		abakusNoType.addActionListener(abakusNoActionListener);
	}
	public void refreshFiler(){
		filterFilEvents.setSelection(logic.getListOfSavedEvents((Type)filType.getSelectedItem()));
		filEvents.setListData(filterFilEvents.getFilteredEntries().toArray());
	}
	public void refreshFilTyper(){
		int selected = filType.getSelectedIndex();
		filType.removeAllItems();
		Type bedpres = null;
		for(Type type : logic.getAllSavedTypes()){
			filType.addItem(type);
			if(type.getId().intValue() == 117)
				bedpres = type;
		}
		if(logic.getAllSavedTypes().size()>0){
			if(selected>0){
				filType.setSelectedIndex(selected);
			} else {
				filType.setSelectedItem(bedpres);
			}
		}
	}
	private void refreshLesere(){
		lesereEvents.setListData(logic.getAllReaders().toArray());
	}
	
	public void entered(){
		//abakusNoEvents.setListData(new Event[0]);
		refreshData();
		lagreToppenStatus.setText("");
		((CardLayout)switchPanel.getLayout()).show(switchPanel, "abakus");
	}
	private void hentFraAbakus(){
		if(abakusNoEvents.getSelectedValue() != null){
			boolean doAction = bedcard.isNotFaded();
			if(logic.getEvent() != null)
				doAction = doAction && sjekkOmSikker("Du vil miste alle endringer du har gjort, er du sikker på at du vil laste en ny?");
			if(doAction){
				bedcard.haltMessage("Laster", "Laster nå ned eventen");
				new Thread(new LasteThread(bedcard, logic, ((Event)abakusNoEvents.getSelectedValue()).getId())).start();
			}
		}
	}
	private void hentListeFraAbakus(){
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		
		Calendar inputFrom = Calendar.getInstance();
		Calendar inputTo = Calendar.getInstance();
		try {	
			inputFrom.setTime(sdf.parse(abakusNoFraDato.getText()));
			inputTo.setTime(sdf.parse(abakusNoTilDato.getText()));
		} catch (ParseException e) {}
		
		from.set(Calendar.YEAR, inputFrom.get(Calendar.YEAR));
		from.set(Calendar.DAY_OF_MONTH, inputFrom.get(Calendar.DAY_OF_MONTH));
		from.set(Calendar.MONTH, inputFrom.get(Calendar.MONTH));
		from.set(Calendar.HOUR, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);

		to.set(Calendar.YEAR, inputTo.get(Calendar.YEAR));
		to.set(Calendar.DAY_OF_MONTH, inputTo.get(Calendar.DAY_OF_MONTH));
		to.set(Calendar.MONTH, inputTo.get(Calendar.MONTH));
		to.set(Calendar.HOUR, 0);
		to.set(Calendar.MINUTE, 0);
		to.set(Calendar.SECOND, 0);
		to.set(Calendar.MILLISECOND, 0);
		if(logic.isAuthenticated()){
			abakusNoEvents.setListData(logic.getListOfEvents(from.getTime(), to.getTime(), (Type)abakusNoType.getSelectedItem()).toArray());
		} else {
			abakusNoEvents.setListData(new Event[0]);
		}
	}
	private void hentFraFil(){
		if(filEvents.getSelectedValue() != null){
			boolean doAction = bedcard.isNotFaded();
			if(logic.getEvent() != null){
				doAction = doAction && sjekkOmSikker("Du vil miste alle endringer du har gjort, er du sikker på at du vil laste en ny?");
			}
			if(doAction){
				Event event = ((BedCardSaveState)filEvents.getSelectedValue()).getEvent();
				logic.loadFromFile(event.getId(), event.getType());
				bedcard.popupMessage("Lastet", "Eventen er nå lastet");
				bedcard.updateInformation();
			}
		}
		refreshData();	
		
	}
	private void connectReader(){
		CardReader reader = (CardReader)lesereEvents.getSelectedValue();
		if(reader != null){
			logic.reconnectReader(reader);
			bedcard.popupMessage("Kobler opp leser", "Prøver å koble opp leser: " + reader.getName());
			refreshLesere();
		}
	}
	private void login(){
		log.debug("Login info satt");
		char[] passord = loginPassord.getPassword();
		String passordString = new String(passord);				
		
		bedcard.haltMessage("Logger på", "Logger nå på abakus.no");
		new Thread(new ConnectThread(bedcard, logic, loginBrukernavn.getText(), passordString)).start();
		
		for(int i = 0; i<passord.length; i++){
			passord[i] = 0;
		}
		loginBrukernavn.setText("");
		loginPassord.setText("");
	}
	private boolean sjekkOmSikker(String melding){
		log.debug("Er du sikker test: " + melding);
		Object[] options = {"Ja", "Nei"};
		String tittel = "Er du sikker?";
		return JOptionPane.showInternalOptionDialog(bedcard.getDesktop(), melding, tittel, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]) == JOptionPane.YES_OPTION;
	}
	@Override
	public void fading(boolean state) {
		faded = state;
		comboBox.setEnabled(!state);

		lesereKonfigBtn.setEnabled(!state);
		abakusKonfigBtn.setEnabled(!state);
		filKonfigBtn.setEnabled(!state);
		
		filType.setEnabled(!state);
		filHent.setEnabled(!state);
		filKunLagrede.setEnabled(!state);
		filLasteInn.setEnabled(!state);

		
		lesereReconnect.setEnabled(!state);
		leserReconnect.setEnabled(!state);

		
		loginBrukernavn.setEnabled(!state);
		loginPassord.setEnabled(!state);
		loginSett.setEnabled(!state);

		logoButton.setEnabled(!state);
		
		if(faded){
			abakusNoLagre.setEnabled(!state);
			filLagre.setEnabled(!state);
			nullstill.setEnabled(!state);
			
			abakusNoFraDato.setEnabled(!state);
			abakusNoTilDato.setEnabled(!state);
			abakusNoType.setEnabled(!state);
			abakusNoHent.setEnabled(!state);
			
			abakusNoLasteInn.setEnabled(!state);
			antallPlasser.setEnabled(!state);
			friFlyt.setEnabled(!state);
			allowAll.setEnabled(!state);
			lagreEventInput.setEnabled(!state);
		} else {
			abakusNoFraDato.setEnabled(logic.isAuthenticated());
			abakusNoTilDato.setEnabled(logic.isAuthenticated());
			abakusNoType.setEnabled(logic.isAuthenticated());
			abakusNoLasteInn.setEnabled(logic.isAuthenticated());
			abakusNoHent.setEnabled(logic.isAuthenticated());
		}
		refreshData();
		
	}

	private final class AbakusNoKonfigureringActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == abakusNoHent){
				hentListeFraAbakus();
			} else if(arg0.getSource() == abakusNoLasteInn){
				hentFraAbakus();
			} else if(arg0.getSource() == loginSett){
				login();			
			} else if(arg0.getSource() == abakusNoType){
				hentListeFraAbakus();
			}
		}
	}
		
	private final class FilKonfigureringActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == filHent){
				refreshFiler();
			} else if(arg0.getSource() == filLasteInn){
				hentFraFil();
			} else if(arg0.getSource() == filType){
				refreshFiler();
			} else if(arg0.getSource() == filKunLagrede){
				filterFilEvents.updateFilter(filKunLagrede.isSelected());
				refreshFiler();
			}
		}
	}
	private final class LesereKonfigureringActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == lesereReconnect){
				logic.reconnectReaders();
				refreshLesere();
				bedcard.popupMessage("Kobler opp lesere", "Prøver å koble opp alle leserene");
			} else if(arg0.getSource() == leserReconnect){
				connectReader();
			}
		}
	}
	private final class KonfigureringMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				if(e.getSource() == lesereEvents){
					connectReader();
				} else if(e.getSource() == filEvents){
					hentFraFil();
				} else if(e.getSource() == abakusNoEvents){
					hentFraAbakus();
				}
			}			
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
		
	}
	private final class KonfigureringSwitcherActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == abakusKonfigBtn){
				((CardLayout)switchPanel.getLayout()).show(switchPanel, "abakus");
				loginBrukernavn.grabFocus();
				refreshAbakus();
			} else if(arg0.getSource() == filKonfigBtn){
				((CardLayout)switchPanel.getLayout()).show(switchPanel, "fil");
			} else if(arg0.getSource() == lesereKonfigBtn){
				((CardLayout)switchPanel.getLayout()).show(switchPanel, "lesere");
				refreshLesere();
			}
			
		}
	}
	
	private final class KonfigureringActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == logoButton){
				if(popupFrame == null && logic.getEvent() != null){
					popupFrame = new JInternalFrame("Velg bilde");
					int popupHeight = 530;
					int popupWidth = 1024;
					int xPos = (bedcard.getDrawWidth()-popupWidth)/2;
					int yPos = (bedcard.getDrawHeight()-popupHeight)/2;
					popupFrame.setBounds(xPos, yPos, popupWidth, popupHeight);
					popupFrame.add(chooser);
					bedcard.getDesktop().add(popupFrame);
					popupFrame.setVisible(true);
				}
			} else if(arg0.getSource() == comboBox){
				bedcard.setDevice((GraphicsDevice)comboBox.getSelectedItem());
			} else if(arg0.getSource() == lagreEventInput){
				try {
					logic.updateMaxParticipants(Integer.parseInt(antallPlasser.getText()));
				} catch (NumberFormatException e) {}
				logic.setFreeFlow(friFlyt.isSelected());
				logic.setAllowEverybody(allowAll.isSelected());
				lagreToppenStatus.setText("Lagret");
				bedcard.updateInformation();
			} else if(arg0.getSource() == nullstill){
				try {
					if(sjekkOmSikker("Dette vil slette all informasjon om eventen, er du sikker?"))
						logic.resetBedCard();					
				} catch (NumberFormatException e) {}
				refreshData();
				bedcard.updateInformation();
			} else if(arg0.getSource() == abakusNoLagre){
				log.debug("Lagre event til abakusNo og fil");
				if(sjekkOmSikker("Dette vil lagre alle endringer til Abakus.no og fil, er du sikker?")){
					bedcard.haltMessage("Lagrer", "Lagrer nå til abakus.no og til fil");
					new Thread(new LagreThread(bedcard, logic)).start();
				}
			} else if(arg0.getSource() == filLagre){
				log.debug("Lagre event til fil");
				if(sjekkOmSikker("Dette vil lagre alle endringer til fil, er du sikker?"))
					if(logic.saveToFile()){
						bedcard.popupMessage("Lagret", "Eventen er nå lagret til fil");
					} else {
						bedcard.popupMessage("Feil", "Klarte ikke å lagre all info til fil");
					}
				refreshFiler();
				refreshFilTyper();
			}
		}
    }
	private final class JFileChooserActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals("ApproveSelection")){
				log.debug("Approve");
				popupFrame.remove(chooser);
				popupFrame.dispose();
				bedcard.getDesktop().remove(popupFrame);
				popupFrame = null;
				
		     	ImageIcon tmpIcon = new ImageIcon(chooser.getSelectedFile().getPath());
		    	Image img = tmpIcon.getImage();
		    	//M� skalere bildet om n�dvendig.
		    	MediaTracker track = new MediaTracker(bedcard);
		    	track.addImage(img, 0);
		    	
		    	try {
					if (track == null)
						log.debug("tracker er null");
					track.waitForAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	Image img2 = ImageScaler.scaleImageToFit(img, 1024, 160);
		    	
		       	logic.getEvent().setImage(img2);
		       	refreshData();
		       	
			} else if(arg0.getActionCommand().equals("CancelSelection")){
				popupFrame.remove(chooser);
				popupFrame.dispose();
				bedcard.getDesktop().remove(popupFrame);
				popupFrame = null;
			}
		}
	}
	private final class KonfigureringDocumentHandler implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
        	lagreToppenStatus.setText("");
        }

        public void removeUpdate(DocumentEvent e) {
        	lagreToppenStatus.setText("");
        }

        public void changedUpdate(DocumentEvent e) {
        }
    }
    private final class KonfigureringChangeListener implements ChangeListener {
    	public void stateChanged(ChangeEvent arg0) {
    		lagreToppenStatus.setText("");
    	}
    }
    
	private final class EnterKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(KeyEvent.VK_ENTER == e.getKeyCode()){
				if(e.getSource() == loginPassord){
					login();
				} else if(e.getSource() == loginBrukernavn){
					loginPassord.grabFocus();
					log.debug("passord, grabFocus");
				} else if(e.getSource() == abakusNoEvents){
					hentFraAbakus();
				} else if(e.getSource() == filEvents){
					hentFraFil();					
				}  else if(e.getSource() == lesereEvents){
					connectReader();					
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
	}
}
