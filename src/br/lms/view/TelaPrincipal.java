package br.lms.view;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import br.lms.core.domain.Lexico;
import br.lms.core.domain.Token;


public class TelaPrincipal extends Shell {
	private Text txtMensagem;
	private Lexico lexico;
	private StyledText txtEditor;
	private Table tableLexico;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			TelaPrincipal shell = new TelaPrincipal(display);
			
			//center the dialog screen to the monitor
			Monitor primary = display.getPrimaryMonitor ();
			Rectangle bounds = primary.getBounds ();
			Rectangle rect = shell.getBounds ();
			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;
			shell.setLocation (x, y);
			
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public TelaPrincipal(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmArquivo = new MenuItem(menu, SWT.CASCADE);
		mntmArquivo.setText("Arquivo");
		
		Menu menu_1 = new Menu(mntmArquivo);
		mntmArquivo.setMenu(menu_1);
		
		MenuItem mntmNovo = new MenuItem(menu_1, SWT.NONE);
		mntmNovo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtEditor.setText("");
				lexico = new Lexico();
			}
		});
		mntmNovo.setText("Novo");
		
		MenuItem mntmAbrir = new MenuItem(menu_1, SWT.NONE);
		mntmAbrir.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				String line_str;
				try {
					JFileChooser fileOpen = new JFileChooser();
					fileOpen.setDialogType(JFileChooser.OPEN_DIALOG);
					int result = fileOpen.showDialog(null, "Open");
					if (result == JFileChooser.APPROVE_OPTION && fileOpen.getSelectedFile().exists()) {
						File file = fileOpen.getSelectedFile();

		                String textoEntrada = "";
		                
		                java.io.FileInputStream isTwo = new java.io.FileInputStream(  "" +file.getPath() );
		                java.io.DataInputStream dsTwo = new java.io.DataInputStream(isTwo);

		                while((line_str = dsTwo.readLine()) != null){
		                    textoEntrada +=  line_str + "\n";
		                }
		                
		                dsTwo.close();
		                
		                txtEditor.setText(textoEntrada.trim());
					}
				} catch (Exception e1) {
				}
			}
		});
		mntmAbrir.setText("Abrir");
		
		MenuItem mntmSalvar = new MenuItem(menu_1, SWT.NONE);
		mntmSalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser arquivo = new JFileChooser();
		        
	            arquivo.setFileSelectionMode( JFileChooser.FILES_ONLY );
	            
	            int resultadoArq = arquivo.showSaveDialog( null );
	            
	            if( resultadoArq == JFileChooser.CANCEL_OPTION)
	                return;
	            
	            File arquivoNome = arquivo.getSelectedFile();
	            
	            if( arquivoNome == null || arquivoNome.getName().equals(""))
	                JOptionPane.showMessageDialog( null, "Nome de Arquivo Inválido",
	                    "LMS", JOptionPane.ERROR_MESSAGE );
	            else{
	                
	                try{
	                    RandomAccessFile file = new RandomAccessFile( arquivoNome, "rw" );
	                    file.writeBytes(txtEditor.getText().trim());
	                    file.close();
	                    
	                }catch(Exception exc){
	                    
	                }
	 
	            }
			}
		});
		mntmSalvar.setText("Salvar");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmFechar = new MenuItem(menu_1, SWT.NONE);
		mntmFechar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		mntmFechar.setText("Fechar");
		
		MenuItem mntmFunes = new MenuItem(menu, SWT.CASCADE);
		mntmFunes.setText("Fun\u00E7\u00F5es");
		
		Menu menu_2 = new Menu(mntmFunes);
		mntmFunes.setMenu(menu_2);
		
		MenuItem mntmAnalisadorLxico = new MenuItem(menu_2, SWT.NONE);
		mntmAnalisadorLxico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executarAnaliseLexica();
			}
		});
		mntmAnalisadorLxico.setText("Analisador L\u00E9xico");		
		
		MenuItem mntmAjuda = new MenuItem(menu, SWT.CASCADE);
		mntmAjuda.setText("Ajuda");
		
		Menu menu_3 = new Menu(mntmAjuda);
		mntmAjuda.setMenu(menu_3);
		
		MenuItem mntmSobre = new MenuItem(menu_3, SWT.NONE);
		mntmSobre.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JOptionPane.showMessageDialog(null, "Desenvolvido por:\nFernando Fogliato", 
						"LMS", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmSobre.setText("Sobre");
		
		Group grpEditor = new Group(this, SWT.NONE);
		grpEditor.setText("Editor");
		grpEditor.setBounds(10, 10, 465, 353);
		
		txtEditor = new StyledText(grpEditor, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		txtEditor.setBounds(10, 24, 445, 319);
		
		Group grpMensagens = new Group(this, SWT.NONE);
		grpMensagens.setText("Mensagens");
		grpMensagens.setBounds(10, 369, 989, 174);
		
		txtMensagem = new Text(grpMensagens, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtMensagem.setEditable(false);
		txtMensagem.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		txtMensagem.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		txtMensagem.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtMensagem.setBounds(10, 30, 969, 134);
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(481, 10, 518, 353);
		
		TabItem tabItemLexico = new TabItem(tabFolder, SWT.NONE);
		tabItemLexico.setText("An\u00E1lise L\u00E9xica");
		
		tableLexico = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabItemLexico.setControl(tableLexico);
		tableLexico.setLinesVisible(true);
		tableLexico.setHeaderVisible(true);
		
		TableColumn tcCodigo = new TableColumn(tableLexico, SWT.NONE);
		tcCodigo.setWidth(53);
		tcCodigo.setText("C\u00F3digo");
		
		TableColumn tcToken = new TableColumn(tableLexico, SWT.NONE);
		tcToken.setWidth(100);
		tcToken.setText("Token");
		
		TableColumn tcDescricao = new TableColumn(tableLexico, SWT.NONE);
		tcDescricao.setWidth(310);
		tcDescricao.setText("Descri\u00E7\u00E3o");
	}

	protected void createContents() {
		setText("LMS");
		setSize(1025, 612);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	/**
	 * Atualiza a tabela de tokens encontrados
	 * @param lista
	 */
	private void atualizaTabelaTokens(ArrayList<Token> lista) {
		tableLexico.setItemCount(0);
		
		for (Token token : lista) {
			TableItem item = new TableItem (tableLexico, 0);
			
			item.setText (new String[] {
					token.getCodigo()+"",
					token.getSimbolo(),
					token.getDescricao()});
		}
	}
	
	/**
	 * Atualiza as mensagens de erro
	 * @param lista
	 */
	private void atualizaMensagensErro(ArrayList<String> lista) {
		
		String mensagem = "";
		
		for (String str : lista) {
			mensagem += str + "\n";
		}
		
		txtMensagem.setText(mensagem);
	}
	
	private void executarAnaliseLexica() {
		String cadeia = txtEditor.getText()+"\n";
		
		lexico = new Lexico();
		lexico.analisarEntrada(cadeia);
		destacarPalavrasReservadas();
		
		atualizaTabelaTokens(lexico.getListaTokensEncontrados());
		atualizaMensagensErro(lexico.getListaMensagensErro());
	}
	
	private void destacarPalavrasReservadas() {    
		
		for (Token token: lexico.getListaTokensEncontrados()) {
			if (lexico.isPalavraReservada(token.getSimbolo())) {
				try {
					StyleRange styleRange = new StyleRange();
					styleRange.start = token.getPosicao();
					styleRange.length = token.getSimbolo().length();
					styleRange.fontStyle = SWT.BOLD;
					styleRange.foreground = SWTResourceManager.getColor(SWT.COLOR_BLACK);
					
					txtEditor.setStyleRange(styleRange);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}		
	}	
}
